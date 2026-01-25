package com.chknkv.feature.main.domain.resolvers

import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.model.presentation.MainUiResult
import com.chknkv.feature.main.model.presentation.uiAction.DetailedStatisticAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Resolver responsible for handling detailed statistics actions.
 *
 * This resolver manages the data loading and pagination for the statistics screen:
 * - Handling requests to close the statistics view
 * - Loading more weight records (pagination) when the user scrolls
 * - Managing loading states and "end of list" indicators
 * - Merging new records with existing ones in the UI state
 *
 * @property interactor Interactor for retrieving paginated weight records.
 */
class DetailedStatisticActionResolver(
    private val interactor: MainScreenInteractor
) : ActionResolver<DetailedStatisticAction> {

    override fun resolve(
        action: DetailedStatisticAction,
        currentState: MainUiResult
    ): Flow<ResolverResult> = flow {
        when (action) {
            is DetailedStatisticAction.HideDetailedStatistic -> {
                emit(ResolverResult.Mutation { it.copy(isDetailedStatisticVisible = false) })
                val idx = currentState.chartWindowIndex
                val chartData = interactor.getChartDataForWindow(idx)
                val canSwipeRight = interactor.hasOlderWindow(idx)
                emit(ResolverResult.Mutation {
                    it.copy(chartData = chartData, chartCanSwipeRight = canSwipeRight)
                })
            }

            is DetailedStatisticAction.LoadMoreWeights -> {
                val currentStats = currentState.detailedStatisticUiResult
                if (!currentStats.isLoading && !currentStats.isEndReached) {
                    emit(ResolverResult.Mutation {
                        it.copy(detailedStatisticUiResult = currentStats.copy(isLoading = true))
                    })

                    try {
                        val currentOffset = currentStats.records.size
                        val newRecords = interactor.getPaginatedWeights(20, currentOffset)
                        val isEndReached = newRecords.size < 20

                        emit(ResolverResult.Mutation {
                            val stats = it.detailedStatisticUiResult
                            it.copy(
                                detailedStatisticUiResult = stats.copy(
                                    records = stats.records + newRecords,
                                    isLoading = false,
                                    isEndReached = isEndReached
                                )
                            )
                        })
                    } catch (_: Exception) {
                        emit(ResolverResult.Mutation {
                            it.copy(detailedStatisticUiResult = it.detailedStatisticUiResult.copy(isLoading = false))
                        })
                    }
                }
            }

            is DetailedStatisticAction.DeleteWeight -> {
                try {
                    interactor.deleteWeight(action.date)
                    val updatedLastWeight = interactor.getLastWeight()
                    emit(ResolverResult.Mutation {
                        val stats = it.detailedStatisticUiResult
                        it.copy(
                            lastSavedWeight = updatedLastWeight,
                            detailedStatisticUiResult = stats.copy(
                                records = stats.records.filter { record -> record.record.date != action.date },
                                isDeleteAlertVisible = false,
                                deleteAlertDate = null
                            )
                        )
                    })
                } catch (_: Exception) {

                }
            }

            is DetailedStatisticAction.ShowDeleteAlert -> {
                emit(ResolverResult.Mutation {
                    val stats = it.detailedStatisticUiResult
                    it.copy(
                        detailedStatisticUiResult = stats.copy(
                            isDeleteAlertVisible = true,
                            deleteAlertDate = action.date
                        )
                    )
                })
            }

            is DetailedStatisticAction.HideDeleteAlert -> {
                emit(ResolverResult.Mutation {
                    val stats = it.detailedStatisticUiResult
                    it.copy(
                        detailedStatisticUiResult = stats.copy(
                            isDeleteAlertVisible = false,
                            deleteAlertDate = null
                        )
                    )
                })
            }
        }
    }
}
