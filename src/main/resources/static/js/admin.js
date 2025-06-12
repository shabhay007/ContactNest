console.log("admin js scripts...");


document.querySelector("#imageFileInput").addEventListener('change', function(event) {

    let file = event.target.files[0];
    let reader = new FileReader();

    reader.onload = function(){
        document.querySelector("#imagePreview").setAttribute("src", reader.result);
    };

    reader.readAsDataURL(file);

});