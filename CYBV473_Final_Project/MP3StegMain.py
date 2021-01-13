'''
Name: Ruben Tequida
Date: 7 December 2020
Version: 2.3
Assignment: CYBV 473 Final Project

Overview: This program allows you to hide a text file within a .wav audio file
and secure it with a password

Relevance to cyber security: Steganography is one of the many tools not only hackers
and those wishing to commit cyber attacks utilize, but also cyber security specialists
use to circumvent cyber attacks or protect their own data. In order to be well rounded
in the field of cyber security you must be aware of all the types of attacks that threaten
your business, network, or devices and how to recognize when they are being used.
I implemented a steganographer that hides text within an audio file to show that
covert messages can be handed off between individuals without outsides being able to
perceive any differences within the modified audio file. Many other types of files
can be hidden within audio files using the same implemenation as was used here.

Instructions:
Once you run the program and the GUI appears there will be two checkboxes near the
top indicating whether you want to hide or extract the text file. Choosing one of
the checkboxes will change the text in each entry field telling you what information
it requires and will enable the processing button at the bottom.

***Audio and text files must be in the same directory as the program***

If hiding the text then you want to put the name of the audio file (must be .wav) in the
'WAV File' entry. Then, in the 'Text File' entry put the name of the text file
you wish to hide within the audio file. Lastely, put the password you wish to lock
down the extraction process with in the 'Password' entry.
Press the process button that says 'HIDE' and the program will hide your
password and text file within the .wav file and create a new audio file that is
called the same as the origianl audio file but with _steg appended to the end of it.
Using the wrong type of audio or text file with result in the status bar (next to the
process button) to display which entry was used incorrectly. If done correclty the
status bar will display 'COMPLETE'

If extracting is seleceted then you want to put the name of the _steg.wav
file in the 'WAV File' entry. Put the name you want for .txt file that will
hold the extracted text in the 'Text File' entry. For the 'Password'
entry put the password you used to hide the file then press the process button which
says 'EXTRACT'. If you put the incorrect password in or use the wrong type of files
the status bar will tell you so. If done correclty the status bar will display
'COMPLETE' and the extracted text will appear in a text file with the given name
in the same directory as the program.
'''

from tkinter import *
from tkinter.font import *
from tkinter import filedialog
import wave

# Main method creates the tkinter GUI and launches the methods for the process button
def main():
    # Defining window size, colors, and fonts
    MAIN_WIN_W = 600
    MAIN_WIN_H = 450
    DEF_TEXT = "gray45"
    COLOR = "steel blue"
    root = Tk()
    root.title("Audio Steger")
    mainFont = Font(family = "Helvetica", size = 36, underline = 1)
    secFont = Font(family = "Helvetica", size = 20)
    basicFont = Font(family = "Helvetica", size = 16)
    # The main canvas everything will appear on
    canvas = Canvas(root, width = MAIN_WIN_W, height = MAIN_WIN_H, bg = COLOR)
    canvas.create_text(MAIN_WIN_W / 2, 40, text = "Audio Steganographer", font = mainFont)
    canvas.create_text(MAIN_WIN_W / 2, 100, text = "Select an Option", font = secFont)
    canvas.create_text(220, 130, text = "Hiding:", font = basicFont)
    # Creating and placing the checkboxes and what to do when clicked
    hide_val = BooleanVar()
    hide_check = Checkbutton(canvas, activebackground = COLOR, bg = COLOR, \
        fg = "blue", var = hide_val, \
        command = lambda: hideClick(hide_val, extract_val, textFileEntry, passFileEntry, wavFileEntry, process))
    hide_check.place(x = 252, y = 119)
    canvas.create_text(340, 130, text = "Extracting:", font = basicFont)
    extract_val = BooleanVar()
    extract_check = Checkbutton(canvas, activebackground = COLOR, bg = COLOR, \
        fg = "blue", var = extract_val, \
        command = lambda: extractClick(hide_val, extract_val, textFileEntry, passFileEntry, wavFileEntry, process))
    extract_check.place(x = 390, y = 119)
    canvas.create_text(90, 180, text = "WAV File:", font = basicFont)
    # Creating and placing all the entry fields and determining their behavior
    wavFileEntry = Entry(canvas, fg = DEF_TEXT)
    wavFileEntry.bind("<FocusIn>", lambda event: on_focusin(0, wavFileEntry, hide_val, 0))
    wavFileEntry.bind("<FocusOut>", lambda event: on_focusout(0, wavFileEntry, hide_val, 0))
    wavFileEntry.place(x = 145, y = 180, width = 350, anchor = W)
    # Adding directory button for easy file accessing
    wavDirButton = Button(canvas, text = ". . .", width = 4, command = lambda: openPath(wavFileEntry))
    wavDirButton.place(x = 530, y = 180, anchor = CENTER)
    canvas.create_text(95, 240, text = "Text File:", font = basicFont)
    textFileEntry = Entry(canvas, fg = DEF_TEXT)
    textFileEntry.bind("<FocusIn>", lambda event: on_focusin(1, textFileEntry, hide_val, 1))
    textFileEntry.bind("<FocusOut>", lambda event: on_focusout(1, textFileEntry, hide_val, 1))
    textFileEntry.place(x = 145, y = 240, width = 350, anchor = W)
    # Adding directory button for easy file accessing
    textDirButton = Button(canvas, text = ". . .", width = 4, command = lambda: openPath(textFileEntry))
    textDirButton.place(x = 530, y = 240, anchor = CENTER)
    canvas.create_text(88, 300, text = "Password:", font = basicFont)
    passFileEntry = Entry(canvas, fg = DEF_TEXT)
    passFileEntry.bind("<FocusIn>", lambda event: on_focusin(3, passFileEntry, hide_val, 1))
    passFileEntry.bind("<FocusOut>", lambda event: on_focusout(3, passFileEntry, hide_val, 1))
    passFileEntry.place(x = 145, y = 300, width = 350, anchor = W)
    # Process button that hides or extracts the information depending on the checkboxes
    process = Button(canvas, width = 8, bg = "gray21", state = DISABLED, command = lambda: processB(wavFileEntry, textFileEntry, passFileEntry, hide_val, statusEntry))
    process.place(x = 200, y = 380, anchor = CENTER)
    canvas.create_text(300, 380, text = "Status:", font = basicFont)
    # Status entry field for displaying errors or completion status
    statusEntry = Entry(canvas)
    statusEntry.place(x = 350, y = 380, width = 120, anchor = W)
    canvas.grid()
    root.mainloop()

# Determines what happens when the 'Hiding' checkbox is clicked
def hideClick(hide_val, extract_val, textFileEntry, passFileEntry, wavFileEntry, process):
    if hide_val.get():
        # Enable the process button for hiding
        process.config(text = "HIDE", state = NORMAL, bg = "gray80")
        # Reseting entry fields to display default information
        wavFileEntry.delete(0, END)
        wavFileEntry.config(fg = "gray45")
        wavFileEntry.insert(0, "Location or name of the .wav file")
        textFileEntry.delete(0, END)
        textFileEntry.config(fg = "gray45")
        textFileEntry.insert(0, "Location or name of the .txt file")
        passFileEntry.delete(0, END)
        passFileEntry.config(fg = "gray45")
        passFileEntry.insert(0, "Enter the password to encrypt the file with")
        # Uncheck extracting checkbox so both aren't checked at same time
        extract_val.set(False)
    else:
        # Disable the process button since neither checkbox is checked
        process.config(text = "", state = DISABLED, bg = "gray21")

# Determines what happens when the 'Extracting' checkbox is clicked
def extractClick(hide_val, extract_val, textFileEntry, passFileEntry, wavFileEntry, process):
    if extract_val.get():
        # Enable the process button for extracting
        process.config(text = "EXTRACT", state = NORMAL,bg = "gray80")
        # Reseting entry fields to display default information
        wavFileEntry.delete(0, END)
        wavFileEntry.config(fg = "gray45")
        wavFileEntry.insert(0, "Location or name of the .wav file")
        textFileEntry.delete(0, END)
        textFileEntry.config(fg = "gray45")
        textFileEntry.insert(0, "Name to save the .txt file as")
        passFileEntry.delete(0, END)
        passFileEntry.config(fg = "gray45")
        passFileEntry.insert(0, "Enter the password used to encrypt the file")
        # Uncheck hiding checkbox so both aren't checked at same time
        hide_val.set(False)
    else:
        process.config(text = "", state = DISABLED, bg = "gray21")

# Opens Windows directory GUI
def openPath(entry):
    filepath = filedialog.askopenfilename()
    entry.config(fg = "black")
    entry.delete(0, END)
    entry.insert(0, filepath)

# Clears entry fields when user clicks to type in them.
def on_focusin(n, entry, hide_val, use):
    text_list = ["Location or name of the .wav file", "Location or name of the .txt file", \
        "Name to save the .txt file as", "Enter the password to encrypt the file with", \
        "Enter the password used to encrypt the file"]
    # Change text if extracting checkbox is checked
    if not hide_val.get() and use:
        n += 1
    if text_list[n] == entry.get():
        entry.delete(0, END)
        entry.config(fg = "black")

# Puts default text back if user leaves field and it is blank.
def on_focusout(n, entry, hide_val, use):
    text_list = ["Location or name of the .wav file", "Location or name of the .txt file", \
        "Name to save the .txt file as", "Enter the password to encrypt the file with", \
        "Enter the password used to encrypt the file"]
    # Change text if extracting checkbox is checked
    if not hide_val.get() and use:
        n += 1
    if entry.get() == "":
        entry.config(fg = "gray45")
        entry.insert(0, text_list[n])

# Method that handles hiding or extracting information depending on which
# checkbox is checked
def processB(wav, text, password, hide, status):
    status.delete(0, END)
    # Getting file names
    wav = wav.get().split("/")[-1]
    textName = text.get().split("/")[-1]
    # Giving error status if .wav or .txt not given
    if not wav.endswith(".wav"):
        status.delete(0, END)
        status.insert(0, ".wav not given")
    elif not textName.endswith(".txt") and hide.get():
        status.delete(0, END)
        status.insert(0, ".txt not given")
    else:
        # Hiding text in audio file
        if hide.get():
            try:
                # Opening and reading text file
                with open(textName, "r") as file:
                    data = file.read()
                # Using gamma codes to determine length of bit stream
                gammaPass = gammaEncode(password.get())
                # Opening audio file and getting it's bits in an array
                wavFile = wave.open(wav, mode = "rb")
                wavBytes = bytearray(list(wavFile.readframes(wavFile.getnframes())))
                gammaData = gammaEncode(data)
                # Hiding the password in the first bits of the audio file
                for i in range(len(gammaPass)):
                    # Clear the LSB of the current byte and put in the bit of the gammaPass
                    wavBytes[i] = (wavBytes[i] & 254) | int(gammaPass[i])
                    n = i
                n += 1
                # Hiding the bits of the text file
                for i in range(len(gammaData)):
                    wavBytes[n] = (wavBytes[n] & 254) | int(gammaData[i])
                    n += 1
                # Collecting all the bytes of the audio file
                modFrame = bytes(wavBytes)
                # Saving the changed bytes to a new audio file
                with wave.open(wav[:-4] + "_steg.wav", "wb") as media:
                    media.setparams(wavFile.getparams())
                    media.writeframes(modFrame)
                wavFile.close()
                status.delete(0, END)
                status.insert(0, "COMPLETE!")
            # Errors opening wave file
            except wave.Error:
                status.delete(0, END)
                status.insert(0, "Wav file error!")
                print("Error loading in", wav, "as a .wav file!")
            # Errors opening text file
            except FileNotFoundError:
                status.delete(0, END)
                status.insert(0, "Text file not found!")
                print ("File", textName, "not found!.")
        else:
            # Extracting text from stegged audio file
            try:
                # Adding .txt to filename if not done by user
                if not textName.endswith(".txt"):
                    textName += ".txt"
                # Opening audio file
                wavFile = wave.open(wav, mode = "rb")
                wavBytes = bytearray(list(wavFile.readframes(wavFile.getnframes())))
                passCount = 0
                dataCount = 0
                # Gamma codes remove first 1 so begin with it
                extractedPass = "1"
                extractedData = "1"
                i = 0
                # Loop through first bits to find length of password bits
                while (wavBytes[i] & 1) == 1:
                    passCount += 1
                    i += 1
                i += 1
                # Get the bits of the password and decode them to ascii
                for n in range(passCount):
                    extractedPass += str(wavBytes[i])
                    i += 1
                extractedPass = bitsToString(extractedPass)
                # If password is wrong display in status and proceed no further
                if extractedPass != password.get():
                    status.delete(0, END)
                    status.insert(0, "Wrong Password")
                    return
                # Get length of text file
                while (wavBytes[i] & 1) == 1:
                    dataCount += 1
                    i += 1
                i += 1
                # Get the bits of the text file, decode to ascii, and write to new text file
                for n in range(dataCount):
                    extractedData += str(wavBytes[i])
                    i += 1
                extractedData = bitsToString(extractedData)
                file = open(textName, 'w')
                file.write(extractedData)
                file.close()
                wavFile.close()
                status.delete(0, END)
                status.insert(0, "COMPLETE!")
            # Errors opening wave file
            except wave.Error:
                status.delete(0, END)
                status.insert(0, "Wav file error!")
                print("Error loading in", wav, "as a .wav file!")

# Method to encode a string of bits into a gamma code
def gammaEncode(num):
    # String to bits
    num = bin(int.from_bytes(num.encode(), "big"))
    # Remove '0b'
    num = num[2:]
    # Get the length of the bit stream and subtract 1 since MSB is always 1
    numLen = len(num)
    gamma = "1" * (numLen - 1) + "0" + num[1:]
    return gamma

# Method to decode bit stream into ascii string
def bitsToString(bits):
    arr = []
    # Getting first octet
    start = len(bits) % 8
    # Adding zeros to it to make it 8 bits
    arr.append("0" * (8 - start) + bits[0 : start])
    # Adding each octect to an array
    for i in range(start, len(bits), 8):
        arr.append(bits[i : i + 8])
    # Joining each octect after it has been changed to ascii
    str = "".join([chr(int(x, 2)) for x in arr])
    return str

main()
