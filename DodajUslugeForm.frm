VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} DodajUslugeForm 
   Caption         =   "Dodaj us³ugê"
   ClientHeight    =   2280
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7800
   OleObjectBlob   =   "DodajUslugeForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "DodajUslugeForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub btnAddItem_Click()
    Dim itemName As String ' nazwa uslugi
    Dim itemPrice As Currency ' cena jednostka
        
    ' Pobranie wartoœci z pól tekstowych
    itemName = NazwaTextBox.text
    itemPrice = CCur(CenaJednostkowaTextBox.text)
    
    If CenaCalkowitaCheckBox.value = True Then ' podana cena jest cena calosciowa
        AddNewItemOnlyPrice itemName, itemPrice
    Else ' podana cena jest cena na osobe (czyli mnozymy ja przez ilosc osob)
        Dim itemQuantity As Integer ' ilosc (osob)
        Dim itemTotal As Currency
        itemQuantity = CInt(IloscTextBox.text)
        itemTotal = itemPrice * itemQuantity
        
        ' Dodanie nowego wiersza do dokumentu
        AddNewItem itemName, itemPrice, itemQuantity, itemTotal, isPersonsCheckBox.value
    End If
 
    ' Wyczyœæ pola tekstowe
    NazwaTextBox.text = ""
    CenaJednostkowaTextBox.text = ""
    IloscTextBox.text = ""
    
    ' Zamknij formularz
    Unload Me
End Sub

Private Sub CenaCalkowitaCheckBox_Click()
    If CenaCalkowitaCheckBox.value = True Then
        IloscTextBox.Visible = False
        isPersonsCheckBox.Visible = False
    Else
        IloscTextBox.Visible = True
        isPersonsCheckBox.Visible = True
    End If
End Sub

Private Sub isPersonsCheckBox_Click()
    If isPersonsCheckBox.value = True Then
        ' Pobierz wartoœæ w³aœciwoœci dokumentu
        Dim ileOsob As String
        ileOsob = GetDocProperty("ile_osob")
        
        ' Ustaw wartoœæ w txtQuantity i zablokuj pole
        IloscTextBox.text = ileOsob
        IloscTextBox.Enabled = False
    Else
        ' Odblokuj pole txtQuantity
        IloscTextBox.Enabled = True
        IloscTextBox.text = ""
    End If
End Sub

Private Sub UserForm_Initialize()
    ' Ustawienie checkboxa jako zaznaczonego
    isPersonsCheckBox.value = True ' usta
    Dim ileOsob As Integer
    ileOsob = GetDocProperty("ile_osob")
    ' Ustawienie wartoœci w txtQuantity i zablokowanie pola
    IloscTextBox.text = ileOsob
    IloscTextBox.Enabled = False
End Sub
