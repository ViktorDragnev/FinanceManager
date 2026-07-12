package com.example.csoftproject.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.models.dtos.AddCategoryDto
import com.example.csoftproject.domain.services.CategoryDataService
import com.example.csoftproject.domain.utils.simulateError
import com.example.csoftproject.domain.validation.ValidationResult
import com.example.csoftproject.domain.validation.category_validators.CategoryValidator.validate
import com.example.csoftproject.ui.state.CategoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddCategoryViewModel(
    private val categoryDataService: CategoryDataService
): ViewModel() {

    private val _addSuccess = MutableStateFlow(false)

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _errorOccurred = MutableStateFlow(false)
    val errorOccurred = _errorOccurred.asStateFlow()

    private val _categoryUiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)

    val validationResult = MutableStateFlow(ValidationResult(
        isValid = false,
        nameError = null
    ))

    fun setName(newName: String) {
        _name.value = newName
    }

    fun addCategory(addCategoryDto: AddCategoryDto, context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {

                _errorOccurred.value = false

                if (simulateError()) {
                    throw Exception()
                }

                if (categoryDataService.categoryExistsByName(addCategoryDto.name)) {
                    Toast.makeText(context, "Such category already exists", Toast.LENGTH_SHORT)
                        .show()
                    _addSuccess.value = false
                } else {
                    categoryDataService.addCategory(addCategoryDto)
                    _addSuccess.value = true
                    onSuccess()
                }

            }catch (_: Exception){
                _categoryUiState.value = CategoryUiState.Error("Category could not be added")
                _errorOccurred.value = true
            }
        }
    }

    fun validateName(name: String) {
        validationResult.value = validate(name)
    }
}