
$(document).ready(function() {
	$("#L_main_layout").attr("style", "background-color:#d4d3d1;");
	var gzWorksWidth = $(".gzworks").width();
	$(".gzworks .gzcontent").width(gzWorksWidth - 2);
	var gzwidth = $(".gznews").width();
	$(".gznews .gzcontent").width(gzwidth - 2);
	
	$(".gzworks .gzrow").mouseenter(function() {
		$(this).addClass("gzrow_selected");
		$(this).find(".gzcaption").addClass("gzcaption_selected");
		$(this).find(".gzdate").addClass("gzdate_selected");
		$(this).find(".gzicon_select").addClass("gzicon_selected");
		$(this).find(".gzicon_calendar").addClass("gzicon_calendar_selected");
	}).mouseleave(function() {
		$(this).removeClass("gzrow_selected");
		$(this).find(".gzcaption").removeClass("gzcaption_selected");
		$(this).find(".gzdate").removeClass("gzdate_selected");
		$(this).find(".gzicon_select").removeClass("gzicon_selected");
		$(this).find(".gzicon_calendar").removeClass("gzicon_calendar_selected");
	});
	
	$(".gzworks .gzdoc").mouseenter(function() {
		$(this).addClass("gzdoc_selected");
	}).mouseleave(function() {
		$(this).removeClass("gzdoc_selected");
	});
	
	//$(".gznews .gzgroup").mouseenter(function() {
		//$(this).addClass("gzgroup_selected");
		//$(this).find(".gzicon_select").addClass("gzicon_selected");
		//$(this).find(".gzicon_calendar").addClass("gzicon_calendar_selected");
	//}).mouseleave(function() {
		//$(this).removeClass("gzgroup_selected");
		//$(this).find(".gzicon_select").removeClass("gzicon_selected");
		//$(this).find(".gzicon_calendar").removeClass("gzicon_calendar_selected");
	//});
});

function initGzRow(initClass) {
	$(".gznews." + initClass + " .gzrow.gzdata").mouseenter(function() {
		$(this).addClass("gzrow_selected");
		$(this).find(".gzicon1").addClass("gzicon_selected");
		$(this).find(".gzicon2").addClass("gzicon_calendar_selected");
		$(this).find(".gzcaption").addClass("gzcaption_selected");
		$(this).find(".gzdate").addClass("gzdate_selected");
	}).mouseleave(function() {
		$(this).removeClass("gzrow_selected");
		$(this).find(".gzicon1").removeClass("gzicon_selected");
		$(this).find(".gzicon2").removeClass("gzicon_calendar_selected");
		$(this).find(".gzcaption").removeClass("gzcaption_selected");
		$(this).find(".gzdate").removeClass("gzdate_selected");
	}).click(function() {
		popIndexArticleView($(this).attr("id"), $(this).find(".gzcaption").html());
	});
}

function popIndexArticleView(id, title) {
	$.post("/portal/pt/indexAction/getArticleById?pkNews=" + id, function(data) {
		popIndexView(title, data);
	});
}

function popIndexWarnView(id, title) {
	$.post("/portal/pt/indexAction/getWarnById?pkWarn=" + id, function(data) {
		popIndexView(title, "<span style='width:100%;height:90%;margin-top:10%;font-family:微软雅黑;font-size:17px;line-height:23px;'>" + data + "</span>");
	});
}

function popIndexView(title, content) {
	var text = "";
	text += "<div class=\"indexPopFloor\"></div>";
	text += "<div class=\"indexPop\">";
		text += "<div class=\"indexPop_head\">";
			text += title;
			text += "<div class=\"close\">X</div>";
		text += "</div>";
		text += "<div class=\"indexPop_body\">";
			text += content;
		text += "</div>";
	text += "</div>";
	$("#L_main_layout").append(text);
	$(".indexPop .close").mouseenter(function() {
		$(this).addClass("close_hover");
	}).mouseleave(function() {
		$(this).removeClass("close_hover");
	}).click(function() {
		$(".indexPopFloor").remove();
		$(".indexPop").remove();
	});
}