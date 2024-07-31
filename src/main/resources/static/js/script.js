/**
 * 
 */

console.log("this is script file")


const togglesidebar=()=>{
   if($('.sidebar').is(":visible")){
  //if true
  //band karna hai
  
  $(".sidebar").css("display" , "none");
  $(".content").css("margin-left" , "2%");


   }
   else{
 //flse
 //show karna hai
 $(".sidebar").css("display" , "block");
 $(".content").css("margin-left" , "20%");

   }
};