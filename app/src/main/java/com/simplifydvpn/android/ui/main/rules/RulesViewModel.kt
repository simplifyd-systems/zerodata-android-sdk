package com.simplifydvpn.android.ui.main.rules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.model.Rule
import com.simplifydvpn.android.data.model.RuleType
import com.simplifydvpn.android.data.repo.RulesRepository
import com.simplifydvpn.android.utils.SingleLiveData
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RulesViewModel : ViewModel() {

    val getCategoriesStatus = MutableLiveData<Status<List<Rule>>>()
    val getDomainsStatus = MutableLiveData<Status<List<Rule>>>()
    val getCustomerRulesStatus = SingleLiveData<Status<Unit>>()
    val loadingStatus = SingleLiveData<Status<Unit>>()

    private val allCategories = mutableListOf<Rule>()
    private val blockedCategories = mutableListOf<Rule>()

    private val blockedDomains = mutableListOf<Rule>()

    private var categories = hashMapOf<String, String>()

    private val rulesRepository = RulesRepository()

    init {
        getCustomerRules()
        getAllCategories()
    }

    fun getCustomerRules() {
        getDomainsStatus.postValue(Status.Loading)

        viewModelScope.launch {
            val result = rulesRepository.getCustomerRules()

            if (result is Status.Success) {
                blockedCategories.addAll(result.data.filter { it.type == RuleType.CATEGORY })
                blockedDomains.addAll(result.data.filter { it.type == RuleType.DOMAIN })

                getDomainsStatus.postValue(Status.Success(blockedDomains))
                generateAndPostCategories()
            }

            if (result is Status.Error) {
                getCustomerRulesStatus.postValue(result)
                getDomainsStatus.postValue(result)
                getCategoriesStatus.postValue(result)
            }
        }
    }

    private suspend fun generateAndPostCategories() {
        withContext(Dispatchers.IO) {
            if (categories.isNotEmpty()) {
                allCategories.addAll(categories.map { entry ->
                    val id = blockedCategories.firstOrNull { entry.key == it.category_id }?.id

                    Rule(id, RuleType.CATEGORY, entry.key, entry.value, null, null)
                })

                getCategoriesStatus.postValue(Status.Success(allCategories.sortedBy { it.category_name }))
            }
        }
    }

    fun getAllCategories() {
        getCategoriesStatus.postValue(Status.Loading)

        viewModelScope.launch {
            val result = rulesRepository.getAllCategories()

            if (result is Status.Success) {
                categories = result.data
                generateAndPostCategories()
            }

            if (result is Status.Error) {
                getCategoriesStatus.postValue(result)
            }
        }
    }

    fun filterCategories(onlyBlocked: Boolean, query: String = "") {
        if (query.isNotEmpty() && onlyBlocked) {
            getCategoriesStatus.postValue(Status.Success(allCategories.filter {
                it.category_name?.contains(query, true) ?: false && it.isBlocked
            }.sortedBy { it.category_name }))
        } else if (query.isNotEmpty() && !onlyBlocked) {
            getCategoriesStatus.postValue(Status.Success(allCategories.filter {
                it.category_name?.contains(query, true) ?: false
            }.sortedBy { it.category_name }))
        } else if (query.isEmpty() && onlyBlocked) {
            getCategoriesStatus.postValue(Status.Success(allCategories.filter { it.isBlocked }
                .sortedBy { it.category_name }))
        } else {
            getCategoriesStatus.postValue(Status.Success(allCategories.sortedBy { it.category_name }))
        }
    }

    fun updateCategoryStatus(category: Rule, onlyBlocked: Boolean) {
        loadingStatus.postValue(Status.Loading)

        viewModelScope.launch {
            var newId: String? = null

            val result = if (category.isBlocked) {
                rulesRepository.deleteRule(category.id!!)
            } else {
                rulesRepository.addCategoryRule(category).also {
                    if (it is Status.Success) {
                        newId = it.data.id
                    }
                }
            }

            if (result is Status.Success) {
                allCategories.firstOrNull { it == category }?.id = newId
                filterCategories(onlyBlocked)
            }

            if (result is Status.Error) {
                getDomainsStatus.postValue(result)
            }
        }
    }

    fun addDomain(domainName: String, action: String) {
        loadingStatus.postValue(Status.Loading)

        viewModelScope.launch {
            val result = rulesRepository.addDomainRule(domainName, action)

            if (result is Status.Success) {
                blockedDomains.add(result.data)
                getDomainsStatus.postValue(Status.Success(blockedDomains))
            }

            if (result is Status.Error) {
                getDomainsStatus.postValue(result)
            }

        }
    }

    fun removeDomain(domain: Rule) {
        loadingStatus.postValue(Status.Loading)

        viewModelScope.launch {
            val result = rulesRepository.deleteRule(domain.id!!)

            if (result is Status.Success) {
                blockedDomains.remove(domain)
                getDomainsStatus.postValue(Status.Success(blockedDomains))
            }

            if (result is Status.Error) {
                getDomainsStatus.postValue(result)
            }
        }
    }
}