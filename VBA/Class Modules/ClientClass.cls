VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "ClientClass"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

' Deklaracja klasy
Private m_clientNumber As Long
Private m_name As String
Private m_street As String
Private m_number As String
Private m_postCode As String
Private m_city As String
Private m_pickupLocation As PickupLocationClass

' Wlasciwosc id
Public Property Get clientNumber() As Long
    clientNumber = m_clientNumber
End Property
Public Property Let clientNumber(value As Long)
    m_clientNumber = value
End Property

' Wlasciwosc name
Public Property Get name() As String
    name = m_name
End Property
Public Property Let name(value As String)
    m_name = value
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

' W³aœciwoœæ pickupLocation
Public Property Get PickupLocation() As PickupLocationClass
    Set PickupLocation = m_pickupLocation
End Property
Public Property Set PickupLocation(value As PickupLocationClass)
    Set m_pickupLocation = value
End Property

' Inicjalizacja obiektu pickupLocation w konstruktorze
Private Sub Class_Initialize()
    Set m_pickupLocation = New PickupLocationClass
End Sub

