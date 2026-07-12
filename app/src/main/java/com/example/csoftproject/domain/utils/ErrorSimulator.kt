package com.example.csoftproject.domain.utils

fun simulateError(): Boolean {
    return (0..100).random() < 30
}