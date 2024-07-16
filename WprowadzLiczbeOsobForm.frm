VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} WprowadzLiczbeOsobForm 
   Caption         =   "Wprowad� liczb� os�b"
   ClientHeight    =   1935
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5310
   OleObjectBlob   =   "WprowadzLiczbeOsobForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "WprowadzLiczbeOsobForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ZatwierdzCommandButton_Click()
    Dim numberOfPeopleText As String
    Dim numberOfPeople As Integer
    
    ' Pobierz wprowadzon� liczb� os�b z TextBoxa
    numberOfPeopleText = Trim(Me.IleOsobTextBox.value)
    
    ' Sprawd� czy wprowadzona warto�� nie jest pusta
    If numberOfPeopleText = "" Then
        MsgBox "Wprowad� liczb� os�b.", vbExclamation, "B��d"
        Exit Sub
    End If
    
    ' Spr�buj przekonwertowa� wprowadzony tekst na liczb�
    On Error Resume Next
    numberOfPeople = CInt(numberOfPeopleText)
    On Error GoTo 0
    
    ' Sprawd� czy uda�o si� przekonwrtowa� na liczbe ca�kowit�
    If Err.Number <> 0 Then
        MsgBox "B��d konwersji.", vbExclamation, "B��d"
        Exit Sub
    End If
    
    ' Sprawd� czy wprowadzona liczba ca�kowita jest liczb� dodatni�
    If numberOfPeople <= 0 Then
        MsgBox "Wprowadzona warto�� nie jest liczb� ca�owit� dodatni�.", vbExclamation, "B��d"
        Exit Sub
    End If
    
    Set doc = ThisDocument ' Ustaw bie��cy dokument Word
    
    UpdateCustomProperty doc, "ile_osob", numberOfPeople
    doc.Fields.Update
    ' Zamknij UserForma po wstawieniu numeru
    Unload Me

End Sub

Private Sub UpdateCustomProperty(doc As Document, propertyName As String, propertyValue As Variant)
    On Error Resume Next
    ' Je�li w�a�ciwo�� istnieje, zaktualizuj j�; w przeciwnym razie dodaj now� w�a�ciwo��
    If doc.CustomDocumentProperties(propertyName).Exists Then
        doc.CustomDocumentProperties(propertyName).value = propertyValue
    Else
        doc.CustomDocumentProperties.Add Name:=propertyName, LinkToContent:=False, _
            Type:=msoPropertyTypeString, value:=propertyValue
    End If
    On Error GoTo 0
End Sub

