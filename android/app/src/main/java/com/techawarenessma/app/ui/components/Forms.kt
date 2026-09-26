package com.techawarenessma.app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.links.isPlausibleEmail
import com.techawarenessma.app.links.newsletterDraft
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

/**
 * A cream, rounded text field. The label sits inside the field so it stays readable on
 * both the light pages and the dark footer. Errors are drawn under the border and set as
 * the field's semantic error, so TalkBack reads them with the field instead of separately.
 */
@Composable
fun TaaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    error: String? = null,
    onDark: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    val shape = RoundedCornerShape(16.dp)
    val errorColor = if (onDark) TaaColors.Coral else TaaColors.Red
    val ring = when {
        error != null -> errorColor
        onDark -> TaaColors.Cream.copy(alpha = 0.4f)
        else -> TaaColors.Ink
    }
    Column(modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, ring, shape)
                .semantics { if (error != null) error(error) },
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            singleLine = singleLine,
            minLines = minLines,
            shape = shape,
            textStyle = TaaType.Body,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = capitalization,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(onAny = { onImeAction?.invoke() }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = TaaColors.Cream,
                unfocusedContainerColor = TaaColors.Cream,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TaaColors.Slate,
                unfocusedTextColor = TaaColors.Slate,
                focusedLabelColor = if (error != null) TaaColors.Red else TaaColors.Blue,
                unfocusedLabelColor = if (error != null) TaaColors.Red else TaaColors.Slate,
                cursorColor = TaaColors.Blue,
            ),
        )
        if (error != null) {
            Text(
                error,
                style = TaaType.Small,
                color = errorColor,
                // Already announced through the field's error semantics.
                modifier = Modifier.padding(start = 16.dp, top = 6.dp).clearAndSetSemantics { },
            )
        }
    }
}

/**
 * "Between workshops." signup. Like the website, it doesn't post anywhere: it opens a
 * pre-filled email to the contact inbox for the user to send.
 */
@Composable
fun NewsletterSignup(modifier: Modifier = Modifier, onDark: Boolean = false) {
    val actions = LocalAppActions.current
    var email by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val submit = {
        if (isPlausibleEmail(email)) {
            error = null
            actions.email(newsletterDraft(email))
        } else {
            error = "Enter an email address like you@example.com."
        }
    }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TaaTextField(
            value = email,
            onValueChange = {
                email = it
                error = null
            },
            label = "Email address for updates",
            placeholder = "you@example.com",
            error = error,
            onDark = onDark,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Send,
            onImeAction = submit,
        )
        PillButton(
            text = "Sign up →",
            onClick = submit,
            style = if (onDark) PillStyle.Yellow else PillStyle.Dark,
        )
    }
}
