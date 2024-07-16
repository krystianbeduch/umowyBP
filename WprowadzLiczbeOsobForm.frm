VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} WprowadzLiczbeOsobForm 
   Caption         =   "WprowadŸ liczbê osób"
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
    
    ' Pobierz wprowadzon¹ liczbê osób z TextBoxa
    numberOfPeopleText = Trim(Me.IleOsobTextBox.value)
    
    ' SprawdŸ czy wprowadzona wartoœæ nie jest pusta
    If numberOfPeopleText = "" Then
        MsgBox "WprowadŸ liczbê osób.", vbExclamation, "B³¹d"
        Exit Sub
    End If
    
    ' Spróbuj przekonwertowaæ wprowadzony tekst na liczbê
    On Error Resume Next
    numberOfPeople = CInt(numberOfPeopleText)
    On Error GoTo 0
    
    ' SprawdŸ czy uda³o siê przekonwrtowaæ na liczbe ca³kowit¹
    If Err.Number <> 0 Then
        MsgBox "B³¹d konwersji.", vbExclamation, "B³¹d"
        Exit Sub
    End If
    
    ' SprawdŸ czy wprowadzona liczba ca³kowita jest liczb¹ dodatni¹
    If numberOfPeople <= 0 Then
        MsgBox "Wprowadzona wartoœæ nie jest liczb¹ ca³owit¹ dodatni¹.", vbExclamation, "B³¹d"
        Exit Sub
    End If
    
    Set doc = ThisDocument ' Ustaw bie¿¹cy dokument Word
    
    UpdateCustomProperty doc, "ile_osob", numberOfPeople
    doc.Fields.Update
    ' Zamknij UserForma po wstawieniu numeru
    Unload Me

End Sub

Private Sub UpdateCustomProperty(doc As Document, propertyName As String, propertyValue As Variant)
    On Error Resume Next
    ' Jeœli w³aœciwoœæ istnieje, zaktualizuj j¹; w przeciwnym razie dodaj now¹ w³aœciwoœæ
    If doc.CustomDocumentProperties(propertyName).Exists Then
        doc.CustomDocumentProperties(propertyName).value = propertyValue
    Else
        doc.CustomDocumentProperties.Add Name:=propertyName, LinkToContent:=False, _
            Type:=msoPropertyTypeString, value:=propertyValue
    End If
    On Error GoTo 0
End Sub

