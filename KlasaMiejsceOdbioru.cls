VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "KlasaMiejsceOdbioru"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
' Deklaracja klasy KlasaMiejsceOdbioru
Private m_miejsce_odbioru As String
Private m_ulica As String
Private m_numer As String
Private m_kod_pocztowy As String
Private m_miejscowosc As String

' Wlasciwosc nazwa_instytucji
Public Property Get miejsce_odbioru() As String
    miejsce_odbioru = m_miejsce_odbioru
End Property
Public Property Let miejsce_odbioru(value As String)
    m_miejsce_odbioru = value
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
