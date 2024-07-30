VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "PickupLocationClass"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
' Deklaracja klasy KlasaMiejsceOdbioru
Private m_pickupLocation As String
Private m_street As String
Private m_number As String
Private m_postalCode As String
Private m_city As String

' Wlasciwosc nazwa_instytucji
Public Property Get pickupLocation() As String
    pickupLocation = m_pickupLocation
End Property
Public Property Let pickupLocation(value As String)
    m_pickupLocation = value
End Property

' Wlasciwosc street
Public Property Get street() As String
    street = m_street
End Property
Public Property Let street(value As String)
    m_street = value
End Property

' Wlasciwosc number
Public Property Get number() As String
    number = m_number
End Property
Public Property Let number(value As String)
    m_number = value
End Property

' Wlasciwosc postalCode
Public Property Get postalCode() As String
    postalCode = m_postalCode
End Property
Public Property Let postalCode(value As String)
    m_postalCode = value
End Property

' Wlasciwosc city
Public Property Get city() As String
    city = m_city
End Property
Public Property Let city(value As String)
    m_city = value
End Property

