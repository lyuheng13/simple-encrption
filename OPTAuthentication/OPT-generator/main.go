package main

import (
	"crypto/sha256"
	"encoding/hex"

	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/data/binding"
	"fyne.io/fyne/v2/widget"
)

var OTP [32]byte
var truncated string
var result *string
var counter int
var output binding.ExternalString

func main() {
	//Initial settings
	counter = 1
	truncated = "N/A"
	result = &truncated

	output = binding.BindString(result)
	app := app.New()

	w := app.NewWindow("OTP Generator")
	c := container.NewVBox(
		widget.NewLabelWithData(output),
		widget.NewButton("Click", generateOTP),
	)
	w.SetContent(c)

	w.ShowAndRun()
}

//Generate one OTP
func generateOTP() {

	if counter == 1 {
		decoded, _ := hex.DecodeString("800770FF00FF08012")
		OTP = sha256.Sum256(decoded)
		truncated = hex.EncodeToString(OTP[:3])
	} else {
		OTP = sha256.Sum256(OTP[:])
		truncated = hex.EncodeToString(OTP[:3])
	}

	//Show the output
	result = &truncated
	output.Reload()

	counter += 1
}
