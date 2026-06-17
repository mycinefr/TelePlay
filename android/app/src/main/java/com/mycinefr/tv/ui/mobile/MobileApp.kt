package com.mycinefr.tv.ui.mobile

import androidx.compose.runtime.Composable
import com.mycinefr.tv.ui.theme.TelePlayMobileTheme

@Composable
fun MobileApp(startDestination: String = "login") {
    TelePlayMobileTheme {
        MobileScaffold(startDestination = startDestination)
    }
}


