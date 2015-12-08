# realm-browser-android

Realm browser that can be used to see tables, structure and content. This browser can be accessed from localhost that hosted by the Android device. To achieve that I'm using NanoHTTPD library.

## Usage

To use this library you can create an instance of RealmBrowser and then call `RealmBrowser.start();` you can also set the port if you want. Don't forget to call `RealmBrowser.stop();` after you finished.

To access RealmBrowser from your browser your Android device and PC/Mac need to be on the same network. And then you can call `realmBrowser.showServerAddress(this);` to show the address in logcat.

## TODO list

1. Implement search function
2. Implement sort function

Any feedbacks, issues or pull requests would be greatly appreciated.

#License

    The MIT License (MIT)

    Copyright (c) 2015 Niko Yuwono

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
