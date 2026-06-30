package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.data.ChallengeEntity
import com.example.myapplication.ui.components.PressableIconButton
import com.example.myapplication.ui.theme.SweetestCatEveFontFamily

@Composable
fun ChallengesScreen(
    challenges: List<ChallengeEntity>,
    onBack: () -> Unit,
    onAddChallenge: (String) -> Unit,
    onUpdateChallenge: (Long, String) -> Unit,
    onDeleteChallenge: (Long) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<ChallengeEntity?>(null) }
    var deleteTarget by remember { mutableStateOf<ChallengeEntity?>(null) }

    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onSave = { description ->
                onAddChallenge(description)
                showAddDialog = false
            }
        )
    }

    editTarget?.let { target ->
        EditChallengeDialog(
            initialText = target.description,
            onDismiss = { editTarget = null },
            onSave = { description ->
                onUpdateChallenge(target.id, description)
                editTarget = null
            }
        )
    }

    deleteTarget?.let { target ->
        DeleteChallengeDialog(
            description = target.description,
            onDismiss = { deleteTarget = null },
            onConfirm = {
                onDeleteChallenge(target.id)
                deleteTarget = null
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2B2B))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, bottom = 4.dp)
            ) {
                PressableIconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color(0xFFFF6A00)
                    )
                }

                Text(
                    text = "Retos",
                    color = Color(0xFFFF6A00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    fontFamily = SweetestCatEveFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 88.dp)
            ) {
                items(challenges, key = { it.id }) { challenge ->
                    ChallengeItem(
                        challenge = challenge,
                        onEdit = { editTarget = challenge },
                        onDelete = { deleteTarget = challenge }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color(0xFFFF8A00),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Text(text = "+", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ChallengeItem(
    challenge: ChallengeEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED))
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsBar,
                    contentDescription = "Reto",
                    tint = Color(0xFFFF8A00),
                    modifier = Modifier.size(24.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    PressableIconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar",
                            tint = Color(0xFF178B73),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    PressableIconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFF9B1C1C),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = Color(0xFFB9B9B9))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = challenge.description, color = Color(0xFF6A6A6A))
        }
    }
}

@Composable
private fun AddChallengeDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val enabled = text.isNotBlank()

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Criterio 2: Título centrado, negro, bold
            Text(
                text = "Agregar reto",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Criterio 3: campo de texto con línea naranja, fondo transparente, texto pegado a la línea
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = "Escriba el reto", color = Color(0xFFAAAAAA)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFFF8A00),
                    unfocusedIndicatorColor = Color(0xFFFF8A00),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Criterios 4 y 5: botones SIN bordes redondeados
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Criterio 4: Cancelar — naranja, sin esquinas redondeadas
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text(text = "Cancelar", color = Color.White, fontWeight = FontWeight.Bold)
                }

                // Criterio 5: Guardar — habilitado/deshabilitado, sin esquinas redondeadas
                Button(
                    onClick = { if (enabled) onSave(text) },
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF5722),
                        disabledContainerColor = Color(0xFFD0D0D0)
                    )
                ) {
                    Text(
                        text = "Guardar",
                        color = if (enabled) Color.White else Color(0xFF9E9E9E),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun EditChallengeDialog(
    initialText: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Editar reto",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFFF8A00),
                    unfocusedIndicatorColor = Color(0xFFFF8A00),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text(text = "Cancelar", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onSave(text) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text(text = "Guardar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DeleteChallengeDialog(
    description: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿Desea eliminar el siguiente reto?:",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = description,
                color = Color(0xFF5E5E5E),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "NO", color = Color(0xFFFF8A00), fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "SI", color = Color(0xFFFF8A00), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

