console.log("Script loaded");

let currentTheme = getTheme();

//event listner add to when screen is opened to call the function
document.addEventListener("DOMContentLoaded" , () => {
    changeTheme();
});
// Initial


function changeTheme() {
    document.querySelector("html").classList.add(currentTheme);

    // Set the listener to change theme button
    const changeThemeButton = document.querySelector("#theme_change_button");

    // Change the text of the button to Dark/Light based on the current theme
    changeThemeButton.querySelector("span").textContent = 
    currentTheme == "light" ? " Dark" : " Light";

    changeThemeButton.addEventListener("click", (event) => {
        console.log("button click");
        const oldTheme = currentTheme;
        if (currentTheme == "dark") {
            // Theme to light
            currentTheme = "light";
        } else {
            // Theme to dark
            currentTheme = "dark";
        }

        // Update localStorage
        setTheme(currentTheme);

        // Remove the old theme
        document.querySelector("html").classList.remove(oldTheme);

        // Set the new theme
        document.querySelector("html").classList.add(currentTheme);

        // Update the button text after changing the theme
        changeThemeButton.querySelector("span").textContent = 
            currentTheme == "light" ? " Dark" : " Light";
    });
}

// Set theme to local storage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

// Get theme from local storage
function getTheme() {
    let theme = localStorage.getItem("theme");
    if (theme) {
        return theme;
    } else {
        return "light";
    }
}
