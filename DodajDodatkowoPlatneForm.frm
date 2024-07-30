VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} DodajDodatkowoPlatneForm 
   Caption         =   "Dodaj 'dodatkowo p³atne w czasie wycieczki'"
   ClientHeight    =   2790
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7800
   OleObjectBlob   =   "DodajDodatkowoPlatneForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "DodajDodatkowoPlatneForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub btnAddItem_Click()
    On Error GoTo ErrorHandler ' Wlaczenie obslugi bledow
    
    Dim unitPrice As Currency ' cena jednostka
        
    ' Pobranie wartosci z pol tekstowych + obsluga bledow
    On Error GoTo ConversionError
    unitPrice = CCur(txtUnitPrice.text)
    On Error GoTo ErrorHandler
    
    Dim currencySymbol As String
    
    ' Sprawdzenie wybranej waluty
    If rbtnPLN.value = True Then
        currencySymbol = "z³"
    ElseIf rbtnEUR.value = True Then
        currencySymbol = "€"
    ElseIf rbtnCZK.value = True Then
        currencySymbol = "Kè"
    End If

    Dim itemQuantity As Integer ' ilosc osob
    Dim totalPrice As Currency
    On Error GoTo ConversionError
    itemQuantity = CInt(txtQuantity.text)
    On Error GoTo ErrorHandler
        
    ' Dodanie nowego wiersza do dokumentu
    totalPrice = unitPrice * itemQuantity
    AddExtraPayableDuringTheTrip unitPrice, itemQuantity, totalPrice, currencySymbol
 
    ' Wyczyœæ pola tekstowe
    txtUnitPrice.text = ""
    txtQuantity.text = ""
    
    ' Zamknij formularz
    Unload Me
    Exit Sub
    
ConversionError:
    MsgBox "Wprowadzono wartosc nie jest liczba." & _
    "Prosze wprowadzic poprawna liczbe", vbExclamation, "Error"
    Exit Sub

ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
    Exit Sub
End Sub

Private Sub UserForm_Initialize()
    Dim ileOsob As Integer
    ileOsob = GetDocProperty("ile_osob")
    ' Ustawienie wartosci w txtQuantity i zablokowanie pola
    txtQuantity.text = ileOsob
    txtQuantity.Enabled = False
    
    ' Ustaw fokus na odpowiednio text boxa
    txtUnitPrice.SetFocus
End Sub


