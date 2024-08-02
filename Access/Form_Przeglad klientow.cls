VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Form_Przeglad klientow"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Compare Database
Option Explicit

Private Sub chk_takie_samo_miejsce_odbioru_AfterUpdate()
    If Me.chk_takie_samo_miejsce_odbioru.Value = True Then
        Me.txt_miejsce_odbioru.Value = "*Adres*"
    Else
        Me.txt_miejsce_odbioru.Value = ""
        Me.txt_ulica_miejsca_odbioru.Value = ""
        Me.txt_numer_miejsca_odbioru.Value = ""
        Me.txt_kod_pocztowy_miejsca_odbioru.Value = ""
        Me.txt_miejscowosc_miejsca_odbioru.Value = ""
    End If
    
    Me.txt_miejsce_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_ulica_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_numer_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_kod_pocztowy_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_miejscowosc_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
End Sub

Private Sub Form_Current()
    If Me.txt_miejsce_odbioru.Value = "*Adres*" Then
        Me.chk_takie_samo_miejsce_odbioru.Value = True
    Else
        Me.chk_takie_samo_miejsce_odbioru.Value = False
    End If
    
    Me.txt_miejsce_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_ulica_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_numer_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_kod_pocztowy_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_miejscowosc_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
End Sub
