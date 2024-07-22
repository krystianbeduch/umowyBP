VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "KlasaZamawiajacy"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
' Deklaracja klasy KlasaZamawiajacy
Private m_id As Integer
Private m_imie As String
Private m_nazwisko As String
Private m_nazwa_instytucji As String
Private m_ulica As String
Private m_numer As String
Private m_kod_pocztowy As String
Private m_miejscowosc As String
Private m_miejsce_odbioru As KlasaMiejsceOdbioru

' Wlasciwosc id
Public Property Get id() As String
    id = m_id
End Property
Public Property Let id(value As String)
    m_id = value
End Property

' Wlasciwosc imie
Public Property Get imie() As String
    imie = m_imie
End Property
Public Property Let imie(value As String)
    m_imie = value
End Property

' Wlasciwosc nazwisko
Public Property Get nazwisko() As String
    nazwisko = m_nazwisko
End Property
Public Property Let nazwisko(value As String)
    m_nazwisko = value
End Property

' Wlasciwosc nazwa_instytucji
Public Property Get nazwa_instytucji() As String
    nazwa_instytucji = m_nazwa_instytucji
End Property
Public Property Let nazwa_instytucji(value As String)
    m_nazwa_instytucji = value
End Property

' Wlasciwosc ulica
Public Property Get ulica() As String
    ulica = m_ulica
End Property
Public Property Let ulica(value As String)
    m_ulica = value
End Property

' Wlasciwosc numer
Public Property Get numer() As String
    numer = m_numer
End Property
Public Property Let numer(value As String)
    m_numer = value
End Property

' Wlasciwosc kod_pocztowy
Public Property Get kod_pocztowy() As String
    kod_pocztowy = m_kod_pocztowy
End Property
Public Property Let kod_pocztowy(value As String)
    m_kod_pocztowy = value
End Property

' Wlasciwosc miejscowosc
Public Property Get miejscowosc() As String
    miejscowosc = m_miejscowosc
End Property
Public Property Let miejscowosc(value As String)
    m_miejscowosc = value
End Property

' W³aœciwoœæ miejsce_odbioru
Public Property Get miejsce_odbioru() As KlasaMiejsceOdbioru
    Set miejsce_odbioru = m_miejsce_odbioru
End Property
Public Property Set miejsce_odbioru(value As KlasaMiejsceOdbioru)
    Set m_miejsce_odbioru = value
End Property

' Inicjalizacja obiektu miejsce_odbioru w konstruktorze
Private Sub Class_Initialize()
    Set m_miejsce_odbioru = New KlasaMiejsceOdbioru
End Sub
