VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "PickupLocationClass"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

' Deklaracja klasy
Private m_pickupLocation As String
Private m_street As String
Private m_number As String
Private m_postCode As String
Private m_city As String

' Wlasciwosc pickupLocation
Public Property Get PickupLocation() As String
    PickupLocation = m_pickupLocation
End Property
Public Property Let PickupLocation(value As String)
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

' Wlasciwosc postCode
Public Property Get postCode() As String
    postCode = m_postCode
End Property
Public Property Let postCode(value As String)
    m_postCode = value
End Property

' Wlasciwosc city
Public Property Get city() As String
    city = m_city
End Property
Public Property Let city(value As String)
    m_city = value
End Property

