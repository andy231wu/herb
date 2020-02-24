
/* handle main menu */
var setActiveColor=true;
$().ready(function(){
    /*below menu*/
    $("#mainMenuItems li:not(.selected)").hover(function(){	/*goodsell */

        $(this).addClass("selected");
    },function(){
        $(this).removeClass("selected");
        highlightSeletedMenu();
        highlightSeletedVMenu();
    });


    highlightSeletedMenu();
    highlightSeletedVMenu();



    $('#verifyImageInput').val('');



});


/*  to handle tab panel */

$(document).ready(function(){
    $(".tabbable").find(".tab").hide();
    /*  var currentTab = location.search.split('anchor=')[1];*/
    var currentTab = location.search.split('anchor=').splice(1).join('').split('&')[0] ;
    if(currentTab){
        var target = "#"+currentTab;
        var nextTabs = $(".tabbable").find(".tabs li").next();
        var nextTabContents = $(".tabbable").find(".tab").next();

        var i;
        for(i=0; i<nextTabs.length; i++) {
            if(target == $(nextTabs[i]).find("a").attr("href") ) {
                $(nextTabContents[i]).show();
                $(nextTabs[i]).find("a").addClass("active");
                break;
            }
        }
    }else{
        $(".tabbable").find(".tab").first().show();
        $(".tabbable").find(".tabs li").first().find("a").addClass("active");
    }

    $(".tabbable").find(".tabs").find("a").click(function(){
        tab = $(this).attr("href");
        $(".tabbable").find(".tab").hide();
        $(".tabbable").find(".tabs").find("a").removeClass("active");
        $(tab).show();
        $(this).addClass("active");
        return false;
    });


});
/*-----Feedback Message -----*/
$(document).ready(function () {
    /* error message did not configure yet*/
    var errorMessage = $(".errorblock");
    if (errorMessage.length > 0) {
        FeedBack.addErrorMessage(errorMessage.text());
    }

    var feedbackMessage = $(".messageblock");
    console.log("feedbackMessage-length: " +feedbackMessage.length);
    if (feedbackMessage.length > 0) {
        FeedBack.addMessage(feedbackMessage.text());
    }
});

var FeedBack = {
    /* omit */
}






/*----- sub functions -----*/
/* main menu */
function highlightSeletedMenu(){
    /* apply selected states depending on current page */
    if (pageUrl.match(/herbalist/)) {
        $("ul#mainMenuItems li:eq(0)").addClass('selected');
    }else if (pageUrl.match(/shop/)) {
        $("ul#mainMenuItems li:eq(1)").addClass('selected');
    }else if (pageUrl.match(/doctor/)) {
        $("ul#mainMenuItems li:eq(2)").addClass('selected');
    } else if (pageUrl.match(/login/)) {
        $("ul#mainMenuItems li:eq(3)").addClass('selected');
    }else if (pageUrl.match(/contactUs/)) {
        $("ul#mainMenuItems li:eq(4)").addClass('selected');
    }else if (pageUrl.match(/userGuide/)) {
        $("ul#mainMenuItems li:eq(5)").addClass('selected');
    } else { /* don't mark any nav links as selected */
        $("ul#mainMenuItems li").removeClass('selected');
    };
}

/* verical menu */
function highlightSeletedVMenu(){

    if (pageUrl.match(/showGlobalParameter/)) { /* Admin site menu */
        $("ul#vMenu li:eq(1)").addClass('selected');
    } else if (pageUrl.match(/editGlobalParameter/)) {
        $("ul#vMenu li:eq(2)").addClass('selected');
    } else if (pageUrl.match(/createShopOwner/)) {
        $("ul#vMenu li:eq(3)").addClass('selected');
    } else if (pageUrl.match(/editShopOwner/)) {
        $("ul#vMenu li:eq(4)").addClass('selected');

    }else { /* don't mark any nav links as selected */
        $("ul#vMenu li").removeClass('selected');
    };
}


function runLoop() {
    $(".photoarea").each(function(){

        //don't run if not enough photos
        if($(this).children().length < 2)
            return;

        var size = $('.photoarea *:first').outerHeight();
        var t = $(this).css('top');
        if(t){
            if(t == 'auto'){
                t = 0;
            }
            else {
                t = t.substring(0,t.length-2);
            }
            var pcThrough = Math.abs(t) / size * 100;
            /*var speed = 80 * (100-pcThrough) / 100 * size; */

            var speed = 30000;
            $('.photoarea:not(:animated)').animate({'top':'-'+ size +'px'}, speed, "linear", function(){
                $('*:first',this).appendTo(this);
                $(this).css({'top':'0px'});
                setTimeout(function(){ runLoop() }, 10);
            }).hover(function(e){
                $(this).stop();
            },function(e){
                setTimeout(function(){ runLoop() }, 10);
            });

            return false;
        }
    });
}

/*  for dropdown servlet */
$(function () {
    $('#dropHerbGroup').on("change", function() {
        var selectedHerbClassName = $('#dropHerbGroup option:selected').val();

        var removedDropdownId = "dropHerbName";
        var uri = "/doctor/searchHerbsByHerbClass";
        herbSearch(selectedHerbClassName, uri, removedDropdownId, populateDropDownData);

    });

    $('#dropHerbName').on("change", function() {
        var selectedHerbName = $('#dropHerbName option:selected').val();
        if(selectedHerbName !== 'null'){
            insertToHerbBox(selectedHerbName);
        }
    });
});

function insertToHerbBox(herbName) {
    var size = $('#herbSize').val();
    var i;
    for (i = 0; i < size; i++) {
        var id = 'herbItems'+ i + '.chineseName';
        var box = document.getElementById(id).value;
        if(!box){
            document.getElementById(id).value=herbName ;
            break;
        }
    }
}
function herbSearch(p1, uri, removedDropdownId, callback) {

    $.ajax({
        url : uri,

        data : {
            param1: p1
        },
        type:"POST",

        success : function(data){
            /* alert("ANDY2-ReturnData: " + data);*/
            callback(data, removedDropdownId);
        },
        error: function(xhr, status){
            alert("Error on Ajax Search - " + status);
        }
    });
}

function populateDropDownData(items, removedDropdownId){
    var caption = $('#selectHerbCaption').val();
    var options = items.split("|");
    var selectBox = document.getElementById(removedDropdownId);
    selectBox.innerHTML = '';

    var el = document.createElement("option");

    el.textContent = caption;
    el.value = "null";
    selectBox.appendChild(el);
    for(var i=0; i < options.length; i++){
        var item = options[i];
        var el = document.createElement("option");
        /*el.textContent = item;*/
        el.text = item;
        el.value = item;

        selectBox.appendChild(el);
    }
}

// -- weekly chart ---
function firstDayLastWeek(){
    populateFirstDayOfWeek(-7);
}

function firstDayNextWeek(){
    populateFirstDayOfWeek(7);
}
function populateFirstDayOfWeek(days){
    var currentWeekDay = document.getElementById("currentDate").textContent;

    var olddate = new Date(currentWeekDay);
    var newdate = new Date(olddate.getFullYear(), olddate.getMonth(), olddate.getDate() + days);
    var dateString = newdate.toLocaleString('en-us', {year: 'numeric', month: '2-digit', day: '2-digit'}).replace(/(\d+)\/(\d+)\/(\d+)/, '$3-$1-$2');
    document.getElementById("currentDate").innerHTML = dateString;

    var currentDay = document.getElementById("currentDate").textContent
    var static1 = "/herbalist/chart/doctor/weekly.png";
    document.getElementById("imageWeekly1").src=static1+ "?startDate="+currentDay;
    var static2 = "/herbalist/chart/doctor/weeklyPatient.png";
    document.getElementById("imageWeekly2").src=static2+ "?startDate="+currentDay;
    var static3 = "/herbalist/chart/doctor/weeklyServiceFee.png";
    document.getElementById("imageWeekly3").src=static3+ "?startDate="+currentDay;

    var futuredate = new Date(newdate.getFullYear(), newdate.getMonth(), newdate.getDate() + 7);
    if(futuredate > new Date()) {
        document.getElementById('nextButton').style.visibility = 'hidden';
    }else{
        document.getElementById('nextButton').style.visibility = 'visible';
    }
}

//--- Monthly chart ---
function firstDayLastMonth(){
    populateFirstDayOfMonth(-1);
}

function firstDayNextMonth(){
    populateFirstDayOfMonth(1);
}
function populateFirstDayOfMonth(months){
    var currentMonthDay = document.getElementById("currentDateM").textContent;

    var olddateM = new Date(currentMonthDay);
    var newdateM = new Date(olddateM.getFullYear(), olddateM.getMonth() + months, olddateM.getDate());
    var dateStringM = newdateM.toLocaleString('en-us', {year: 'numeric', month: '2-digit', day: '2-digit'}).replace(/(\d+)\/(\d+)\/(\d+)/, '$3-$1-$2');
    document.getElementById("currentDateM").innerHTML = dateStringM;

    var currentDayM = document.getElementById("currentDateM").textContent
    var staticM1 = "/herbalist/chart/doctor/monthlyPatient.png";
    document.getElementById("imageMonthly1").src=staticM1+ "?startDate="+currentDayM;
    var staticM2 = "/herbalist/chart/doctor/monthlyServiceFee.png";
    document.getElementById("imageMonthly2").src=staticM2+ "?startDate="+currentDayM;

    var futuredateM = new Date(newdateM.getFullYear(), newdateM.getMonth() + 1, newdateM.getDate());
    if(futuredateM > new Date()) {
        document.getElementById('nextButtonM').style.visibility = 'hidden';
    }else{
        document.getElementById('nextButtonM').style.visibility = 'visible';
    }
}

//--- Yearly chart ---
function firstDayLastYear(){
    populateFirstDayOfYear(-1);
}

function firstDayNextYear(){
    populateFirstDayOfYear(1);
}
function populateFirstDayOfYear(years){
    var currentYearDayY = document.getElementById("currentDateY").textContent;

    var olddateY = new Date(currentYearDayY);
    var newdateY = new Date(olddateY.getFullYear() + years, olddateY.getMonth(), olddateY.getDate());
    var dateStringY = newdateY.toLocaleString('en-us', {year: 'numeric', month: '2-digit', day: '2-digit'}).replace(/(\d+)\/(\d+)\/(\d+)/, '$3-$1-$2');
    document.getElementById("currentDateY").innerHTML = dateStringY;

    var currentDayY = document.getElementById("currentDateY").textContent
    var staticY1 = "/herbalist/chart/doctor/yearly.png";
    document.getElementById("imageYearly1").src=staticY1+ "?startDate="+currentDayY;
    var staticY2 = "/herbalist/chart/doctor/yearlyPatient.png";
    document.getElementById("imageYearly2").src=staticY2+ "?startDate="+currentDayY;
    var staticY3 = "/herbalist/chart/doctor/yearlyServiceFee.png";
    document.getElementById("imageYearly3").src=staticY3+ "?startDate="+currentDayY;


    var futuredateY = new Date(newdateY.getFullYear() + 1, newdateY.getMonth(), newdateY.getDate());
    if(futuredateY > new Date()) {
        document.getElementById('nextButtonY').style.visibility = 'hidden';
    }else{
        document.getElementById('nextButtonY').style.visibility = 'visible';
    }
}

//=== for delete prescription confirm dialog box
$(document).ready(function(){

    $(".column-right").on("click", "#delete-prescription-link", function(e) {
        e.preventDefault();

        var prescriptionDeleteDialogTempate = Handlebars.compile($("#template-delete-prescription-confirmation-dialog").html());

        $("#view-holder").append(prescriptionDeleteDialogTempate());
        $("#delete-prescription-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-prescription-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-prescription-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-prescription-button", function(e) {
        e.preventDefault();
        var year = document.getElementById("year-id").value;
        if(year == ""){
            year = 9999;
        }
        window.location.href = "/doctor/purgeOldPrescriptions/"+ year;
    });
});




