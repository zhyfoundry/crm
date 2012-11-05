function sendMail(e) {
	//TODO check to see if there are currently any active uploads

	$("#composeForm").submit();
}

var uploadIframeCount = 0;
function addNewUpload() {
	$('#attachmentstr').show();

	//get filename
	var f=$('#inputfile').val();
	var pos=f.lastIndexOf('\\');
	if(pos>=0) f=f.substr(pos+1);
	pos=f.lastIndexOf('/');
	if(pos>=0) f=f.substr(pos+1);

	//add attachment to list
	var li=document.createElement("li");
	li.id="uploadli"+uploadIframeCount;
	var img=document.createElement("img");
	img.src=uploadingGif;
	li.appendChild(img);
	var sp=document.createElement("span");
	sp.innerHTML=f;
	sp.style.paddingRight=5;
	li.appendChild(sp);
	var a=document.createElement("a");
	a.href="javascript:removeAttach('"+f+"')";
	a.innerHTML=" "+txtRemove;
	a.style.color='#5A799E';
	a.style.display='none';
	li.appendChild(a);
	var list=$("#composeAttachmentList");
	list.append(li);

	var theFile = document.getElementById("inputfile");
	var fileParent = theFile.parentNode;
	var theDiv = document.createElement('div');
	theDiv.style.display = 'none';
	theDiv.innerHTML = '<iframe id="uploadiframe'+uploadIframeCount+'" name="uploadiframe'+uploadIframeCount+'" src=""></iframe>' +
	    '<form id="uploadform'+uploadIframeCount+'" target="uploadiframe'+uploadIframeCount+'" action="' + uploadAttachment +'" enctype="multipart/form-data" method="post">' +
	    '<input type="hidden" name="iframeid" id="iframeid" value="'+uploadIframeCount+'"/>' +
	    '</form>';
	$('#uploader').append(theDiv);
	var uploadform = document.getElementById("uploadform"+uploadIframeCount);
	fileParent.removeChild(theFile);
	uploadform.appendChild(theFile);
	uploadIframeCount++;
	uploadform.submit();
	uploadform.removeChild(theFile);
	fileParent.appendChild(theFile);

//	fixLayout();
}

function removeAttach(fileName) {

	var lis = $('#composeAttachmentList li');

	for (var i=0;i<lis.length;i++) {
		var li = lis[i];

		if (li.innerHTML.indexOf(fileName) >= 0) {
			li.style.display = "none";

			var actCount = 0;
			if (lis.length > 1) {
				for (var j=0;j<lis.length;j++) {
					if (lis[j].style.display != 'none') {
						actCount++;
					}
				}
			}
			if (actCount == 0) {
				$('#attachmentstr')[0].style.display = 'none';
			}
		}
	}

	// remove it from the server
	var paramData = "f=" + myEncode(fileName);

	$.ajax({
		type : "POST",
		url : deleteAttachment + "?" + paramData,
		success : function(data) {
//			fixLayout();
		},
		error : function() {
			alert('error!');
		}
	});
}

function myEncode(str) {
	str = encodeURI(str);
	str = str.replace(new RegExp("&",'g'), "%26");
	str = str.replace(new RegExp(";",'g'), "%3B");
	str = str.replace(new RegExp("[?]",'g'), "%3F");
	return str;
}