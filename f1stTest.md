<window id="win" title="My First Window" border="normal" width="600px"
> sizable="true">
> 

&lt;vbox id="gbH22" width="100%"&gt;


> > 

&lt;zscript&gt;


> > > Map myMap = new java.util.HashMap(); String a = "";
> > > myMap.put("win", "a"); myMap.put("alert('a')", "b");
> > > myMap.put("printf()", "c"); //myMap .addAll
> > > (com.oxseed.zk.sandbox.CodeCache.getCache()); ListModel
> > > codesTmp = new
> > > org.zkoss.zkplus.databind.BindingListModelMap( myMap, true);

> > 

&lt;/zscript&gt;




> 

&lt;vbox id="gb0" width="100%"&gt;



> 

&lt;hbox id="gbH2" width="100%"&gt;


> > 

&lt;vbox id="gb11" width="100%"&gt;


> > > <listbox id="list" width="100%"
> > > > model="&#36;{codesTmp}"
> > > > onClick="codeView.value=  a+self.selectedItem.label;  ">
> > > > 

&lt;listhead&gt;


> > > > > <listheader label="Load on Demend"
> > > > > > sort="auto" />

> > > > 

&lt;/listhead&gt;



> > > 

Unknown end tag for &lt;/listbox&gt;


> > > 

&lt;splitter id="s12" collapse="before" width="100%" /&gt;





> 

&lt;/vbox&gt;



> 

&lt;splitter id="s2" collapse="before" width="100%" /&gt;



> 

&lt;vbox id="gb2" width="100%"&gt;


> > 

&lt;textbox id="codeView" rows="10" width="100%"&gt;


> > > 

&lt;attribute name="value"&gt;








> <![CDATA[ <window title="My First Window"
> border="normal" width="200px"> don't edit
> this part. the value will be replaced by
> application starting. 

Unknown end tag for &lt;/window&gt;

 ]]>




> 

&lt;/attribute&gt;


> 

&lt;/textbox&gt;


> 

&lt;splitter id="s3" collapse="before" /&gt;


> Column 1-2: You can enforce to open or collapse
> programming by calling setOpen method.
> 

&lt;/vbox&gt;


> 

&lt;/hbox&gt;



> 

&lt;/vbox&gt;




> 

&lt;hbox&gt;


> > <button label="Overlap"
> > > onClick="win.setSizable(true);win.doOverlapped();" />

> > <button label="Popup"
> > > onClick="win.setSizable(true);win.doPopup();" />

> > <button label="Embed"
> > > onClick="win.setSizable(false);win.doEmbedded();" />

> > <button label="Redraw"
> > > onClick="win.invalidate(); gbH22.invalidate(); " />


> Hello, World!
> 

&lt;/hbox&gt;


> 

&lt;/vbox&gt;




Unknown end tag for &lt;/window&gt;

