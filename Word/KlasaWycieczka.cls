VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "KlasaWycieczka"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
' Deklaracja klasy KlasaWycieczka
Private m_nr As String
Private m_nazwaWycieczki As String
Private m_cenaBazowa As Integer
Private m_biletyWstepu As Integer

' Wlasciwosc nr
Public Property Get nr() As String
    nr = m_nr
End Property
Public Property Let nr(value As String)
    m_nr = value
End Property

' Wlasciwosc nazwa_wycieczki
Public Property Get nazwa_wycieczki() As String
    nazwa_wycieczki = m_nazwaWycieczki
End Property
Public Property Let nazwa_wycieczki(value As String)
    m_nazwaWycieczki = value
End Property

' Wlasciwosc cena_bazowa
Public Property Get cena_bazowa() As Double
    cena_bazowa = m_cenaBazowa
End Property
Public Property Let cena_bazowa(value As Double)
    m_cenaBazowa = value
End Property

' Wlasciwosc bilety_wstepu
Public Property Get bilety_wstepu() As Integer
    bilety_wstepu = m_biletyWstepu
End Property
Public Property Let bilety_wstepu(value As Integer)
    m_biletyWstepu = value
End Property