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
    
    ' Pobierz wprowadzona liczbe osob z TextBoxa
    numberOfPeopleText = Trim(Me.IleOsobTextBox.value)
    
    ' Sprawdz czy wprowadzona wartosc nie jest pusta
    If numberOfPeopleText = "" Then
        MsgBox "WprowadŸ liczbê osób.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Sprobuj przekonwertowac wprowadzony tekst na liczbe
    On Error Resume Next
    numberOfPeople = CInt(numberOfPeopleText)
    On Error GoTo 0
    
    ' Sprawdz czy udalo siê przekonwrtowac na liczbe ca³kowita
    If Err.Number <> 0 Then
        MsgBox "Blad konwersji.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Sprawdz czy wprowadzona liczba calkowita jest liczba dodatnia
    If numberOfPeople <= 0 Then
        MsgBox "Wprowadzona wartosc nie jest liczba calowita dodatnia.", vbExclamation, "Error"
        Exit Sub
    End If
    
    Set doc = ThisDocument ' Ustaw biezacy dokument Word
    
    UpdateCustomProperty doc, "ile_osob", numberOfPeople
    doc.Fields.Update
    ' Zamknij UserForma po wstawieniu numeru
    Unload Me

End Sub

Private Sub UpdateCustomProperty(doc As Document, propertyName As String, propertyValue As Variant)
    On Error Resume Next
    ' Jesli wlasciwosc istnieje, zaktualizuj ja; w przeciwnym razie dodaj nowa wlasciwosc
    If doc.CustomDocumentProperties(propertyName).Exists Then
        doc.CustomDocumentProperties(propertyName).value = propertyValue
    Else
        doc.CustomDocumentProperties.Add Name:=propertyName, LinkToContent:=False, _
            Type:=msoPropertyTypeString, value:=propertyValue
    End If
    On Error GoTo 0
End Sub

