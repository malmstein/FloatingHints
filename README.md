FloatingHints
=============

Custom implementation of the Floating Label pattern.

**Customize the animation** used to show and hide the label via XML

![Demo gif](https://raw.githubusercontent.com/malmstein/FloatingHints/master/art/floating_fade.gif)

Introduced in a series of posts about [Forms in Android](http://www.malmstein.com/blog/2014/06/09/your-forms-dont-need-to-be-ugly-part-2/)

Dependencies
------------

To get the current snapshot version:

```groovy
buildscript {
    repositories {    
        mavenCentral()        
        maven {        
            url "https://oss.sonatype.org/content/repositories/snapshots/"            
        }        
    }    
    dependencies {    
        classpath 'com.malmstein:floatinghints:0.0.1-SNAPSHOT'        
    }    
}
```

Usage
-----

```xml
  <com.malmstein.floatinghints.FloatEditTextLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:floatLayoutInAnimation="@anim/abc_fade_in"
    app:floatLayoutOutAnimation="@anim/abc_fade_out"
    app:floatLayoutTextAppearance="@style/FloatLabel.Label">

    <EditText
      style="@style/FloatLabel.Form.Text"
      android:id="@+id/float_login_email"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:hint="@string/hint_email"
      android:inputType="textEmailAddress" />

  </com.malmstein.floatinghints.FloatEditTextLayout>

```

Styling
-------

You can use your own styiling for the EditText, in addition to this there are some attributes
that can be edited from the FloatingLayout

```xml
<declare-styleable name="FloatEditTextLayout">
  <attr name="floatLayoutTextAppearance" format="reference" />
  <attr name="floatLayoutInAnimation" format="reference" />
  <attr name="floatLayoutOutAnimation" format="reference" />
  <attr name="floatLayoutSidePadding" format="reference|dimension" />

```

By modifying the `floatLayoutInAnimation` and `floatLayoutOutAnimation` one can set the custom 
animation that will make the floating label to appear and dissappear.


Acknowledgements
----------------

* Thanks to [Chris Banes][1] for the initial implementation.
* Thanks to [Cyril Mottier][2] for starting the debate on App Polishing.
* Thanks to [Matt D. Smith][3] for the iOS implementation posted on Dribble
* Thanks to [Brad Frost Ling][4] for the post introducing the Float Label Pattern.

License
-------

    Copyright 2014 David Gonzalez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


---

 [1]: https://plus.google.com/+ChrisBanes/posts/5Ejaq51UWGo
 [2]: https://plus.google.com/118417777153109946393/posts/ewdTd7bNw29
 [3]: https://dribbble.com/shots/1254439--GIF-Mobile-Form-Interaction
 [4]: http://bradfrostweb.com/blog/post/float-label-pattern/