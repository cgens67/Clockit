package com.clockit.cgens67.domain.model

sealed class NumberKeypadOperation {
    class AddNumber(val number: String) : NumberKeypadOperation()
    object Delete : NumberKeypadOperation()
    object Clear : NumberKeypadOperation()
}
