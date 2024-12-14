const viewContactModal= document.getElementById("view_contact_model");

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'viewContactModal',
  override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(id){
    console.log(id);

    try {
        const data = await(await fetch(`http://localhost:8081/api/contacts/${id}`)).json();

        document.getElementById('contact-name').textContent = data.name;
        document.getElementById('contact-email').textContent = data.email;
        document.getElementById('contact-phone').textContent = data.phoneNumber;
        document.getElementById('contact-address').textContent = data.address;
        document.getElementById('contact-description').textContent = data.description;
        document.getElementById('contact-picture').src = data.picture;
        document.getElementById('contact-website').href = data.websiteLink;
        document.getElementById('contact-website').textContent = data.websiteLink;
        document.getElementById('contact-linkedin').href = data.linkedInLink;
        document.getElementById('contact-linkedin').textContent = data.linkedInLink;
        document.getElementById('contact-favourite').textContent = data.favourite ? 'Yes' : 'No';
        openContactModal();

    } catch (error) {
        console.log(error);
    }   
}

// DELETE MODAL
const deleteContactModal = document.getElementById("delete_modal");

// Options for the delete modal
const deleteModalOptions = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('Delete modal is hidden');
    },
    onShow: () => {
        console.log('Delete modal is shown');
    },
    onToggle: () => {
        console.log('Delete modal has been toggled');
    },
};

// Instance options object
const deleteInstanceOptions = {
    id: 'deleteContactModal',
    override: true,
};

// Initialize the modal
const deleteContactInstance = new Modal(deleteContactModal, deleteModalOptions, deleteInstanceOptions);

let contactIdToDelete = null; // To store the ID of the contact to be deleted

// Function to open the delete confirmation modal
function openDeleteContactModal(id) {
    contactIdToDelete = id; // Store the contact ID for deletion
    deleteContactInstance.show();
}

// Function to close the delete modal
function closeDeleteContactModal() {
    deleteContactInstance.hide();
}

// Function to confirm deletion
async function confirmDeleteContact() {
    if (!contactIdToDelete) {
        console.error("No contact ID to delete");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8081/user/contacts/delete/${contactIdToDelete}`, {
            method: 'DELETE',
        });

        if (response.ok) {
            console.log(`Contact with ID ${contactIdToDelete} deleted successfully`);
            deleteContactInstance.hide(); // Close the modal
            // Optionally, refresh the contact list or redirect
            window.location.reload(); // Reload the page to reflect changes
        } else {
            console.error(`Failed to delete contact with ID ${contactIdToDelete}`);
        }
    } catch (error) {
        console.error(`Error deleting contact: ${error}`);
    }
}


 