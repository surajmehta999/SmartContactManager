console.log("Admin JS");

document.querySelector("#image_file_input").addEventListener("change", function (event) {
    let file = event.target.files[0];
    if (file && file.type.startsWith("image/")) { // Check if the selected file is an image
        let reader = new FileReader();
        reader.onload = function () {
            document.getElementById("upload_image_preview").src = reader.result; // Set preview image
        };
        reader.readAsDataURL(file);
    } else {
        alert("Please select a valid image file."); // Notify if file is invalid
        event.target.value = ""; // Clear the input
        document.getElementById("upload_image_preview").src = ""; // Clear the preview
    }
});
