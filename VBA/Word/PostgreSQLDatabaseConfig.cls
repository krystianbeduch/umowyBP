VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "PostgreSQLDatabaseConfig"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Private m_Server As String
Private m_Port As String
Private m_Database As String
Private m_User As String
Private m_Password As String

' Wlasciwosc Server
Public Property Get Server() As String
    Server = m_Server
End Property

Public Property Let Server(ByVal value As String)
    m_Server = value
End Property

' Wlasciwosc Port
Public Property Get Port() As String
    Port = m_Port
End Property

Public Property Let Port(ByVal value As String)
    m_Port = value
End Property

' Wlasciwosc Database
Public Property Get Database() As String
    Database = m_Database
End Property

Public Property Let Database(ByVal value As String)
    m_Database = value
End Property

' Wlasciwosc User
Public Property Get User() As String
    User = m_User
End Property

Public Property Let User(ByVal value As String)
    m_User = value
End Property

' Wlasciwosc Password
Public Property Get Password() As String
    Password = m_Password
End Property

Public Property Let Password(ByVal value As String)
    m_Password = value
End Property

' Wlasciwosc ConnectionString
Public Property Get ConnectionString() As String
    ConnectionString = "Driver={PostgreSQL Unicode};" & _
                       "Server=" & m_Server & ";" & _
                       "Port=" & m_Port & ";" & _
                       "Database=" & m_Database & ";" & _
                       "Uid=" & m_User & ";" & _
                       "Pwd=" & m_Password & ";"
End Property

