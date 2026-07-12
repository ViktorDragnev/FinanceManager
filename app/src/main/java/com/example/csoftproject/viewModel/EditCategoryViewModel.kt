package com.example.csoftproject.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.domain.services.CategoryDataService
import com.example.csoftproject.domain.utils.simulateError
import com.example.csoftproject.domain.validation.ValidationResult
import com.example.csoftproject.domain.validation.category_validators.CategoryValidator.validate
import com.example.csoftproject.ui.state.CategoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditCategoryViewModel(
    private val categoryDataService: CategoryDataService
) : ViewModel() {

    private val _errorOccurred = MutableStateFlow(false)
    val errorOccurred = _errorOccurred.asStateFlow()

    private val _updateSuccess = MutableStateFlow(false)

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    val validationResult = MutableStateFlow(ValidationResult(
        isValid = false,
        nameError = null
    ))

    val categories = categoryDataService.getCategoryFlow()

    private val _categoryUiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)

    fun setName(newName: String) {
        _name.value = newName
    }

    fun updateCategory(category: Category, context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {

                _errorOccurred.value = false


                if (simulateError()) {
                    throw Exception()
                }

                if (categoryDataService.categoryExistsByName(category.name, category.id)) {
                    Toast.makeText(context, "Such category already exists", Toast.LENGTH_SHORT)
                        .show()
                    _updateSuccess.value = false
                } else {
                    categoryDataService.updateCategory(category)
                    _updateSuccess.value = true
                    onSuccess()
                }

            }catch (_: Exception){
                _categoryUiState.value = CategoryUiState.Error("Category could not be updated")
            }
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            try {
                categoryDataService.deleteCategory(id)
            }catch (_: Exception){
                _categoryUiState.value = CategoryUiState.Error("Category could not be deleted")
            }
        }
    }

    fun validateName(name: String) {
        validationResult.value = validate(name)
    }
}