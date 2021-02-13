package com.simplifydvpn.android.data.repo

import com.simplifydvpn.android.data.model.Rule
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.handleError

class RulesRepository : BaseRepository() {

    suspend fun getCustomerRules(): Status<List<Rule>> {
        return try {
            val rules = apiService.getCustomerRules()
            Status.Success(rules)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    suspend fun getAllCategories(): Status<HashMap<String, String>> {
        return try {
            val categories = apiService.getAllCategories()
            Status.Success(categories)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    suspend fun addCategoryRule(category: Rule): Status<Rule> {
        return try {
            val rule = apiService.addRule(
                hashMapOf(
                    "type" to "category",
                    "category_id" to category.category_id!!,
                    "category_name" to category.category_name!!
                )
            )
            Status.Success(rule)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    suspend fun addDomainRule(domainName: String, action: String): Status<Rule> {
        return try {
            val rule = apiService.addRule(
                hashMapOf(
                    "type" to "domain",
                    "domain" to domainName,
                    "action" to action
                )
            )
            Status.Success(rule)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    suspend fun deleteRule(id: String): Status<Unit> {
        return try {
            apiService.deleteRule(id)
            Status.Success(Unit)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }
}