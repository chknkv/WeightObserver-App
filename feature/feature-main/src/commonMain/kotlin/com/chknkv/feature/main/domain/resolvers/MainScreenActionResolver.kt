package com.chknkv.feature.main.domain.resolvers

import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.model.presentation.uiResult.DetailedStatisticUiResult
import com.chknkv.feature.main.model.presentation.MainUiResult
import com.chknkv.feature.main.model.presentation.uiResult.MeasurementUiResult
import com.chknkv.feature.main.model.presentation.uiAction.MainScreenAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainScreenActionResolver(
    private val interactor: MainScreenInteractor
) : ActionResolver<MainScreenAction> {

    override fun resolve(
        action: MainScreenAction,
        currentState: MainUiResult
    ): Flow<ResolverResult> = flow {
        when (action) {
            is MainScreenAction.Init -> {
                val lastWeight = interactor.getLastWeight()
                emit(ResolverResult.Mutation { it.copy(lastSavedWeight = lastWeight) })
            }

            is MainScreenAction.ShowSettings -> {
                emit(ResolverResult.Mutation { it.copy(isSettingVisible = true) })
            }

            is MainScreenAction.ShowAddMeasurement -> {
                emit(ResolverResult.Mutation {
                    it.copy(
                        isAddMeasurementVisible = true,
                        measurementUiResult = MeasurementUiResult()
                    )
                })
            }
            is MainScreenAction.ShowDetailedStatistic -> {
                emit(ResolverResult.Mutation {
                    it.copy(
                        isDetailedStatisticVisible = true,
                        detailedStatisticUiResult = DetailedStatisticUiResult(isLoading = true)
                    )
                })

                try {
                    val newRecords = interactor.getPaginatedWeights(20, 0)
                    val isEndReached = newRecords.size < 20
                    emit(ResolverResult.Mutation {
                        val stats = it.detailedStatisticUiResult
                        it.copy(
                            detailedStatisticUiResult = stats.copy(
                                records = newRecords,
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
    }
}
