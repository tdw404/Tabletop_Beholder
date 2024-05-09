import Authenticator from '../api/authenticator';
import MaterialsClient from '../api/materialsClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const ORG_ID_KEY = 'organization-id-key';
const MATERIAL_LIST_KEY = 'material-list-key';
const EMPTY_DATASTORE_STATE = {
    [ORG_ID_KEY]: '',
    [MATERIAL_LIST_KEY]: []
};


class ManageMaterials extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['editMaterial', 'mount', 'editButton', 'addMaterial', 'addButton', 'deleteButton',
         'deleteMaterial', 'startupActivities', 'populateTable'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
    }


    mount() {
        document.getElementById('edit-btn').addEventListener('click', this.editButton);
        document.getElementById('add-btn').addEventListener('click', this.addButton);
        document.getElementById('delete-btn').addEventListener('click', this.deleteButton);
        this.materialsClient = new MaterialsClient();
        this.header.addHeaderToPage();
        this.startupActivities();
    }

    async startupActivities() {

            const orgId = new URLSearchParams(window.location.search).get('orgId');

            this.dataStore.set([ORG_ID_KEY], orgId)
            this.dataStore.set([MATERIAL_LIST_KEY], await this.materialsClient.getMultipleMaterials(orgId));
            await this.populateTable();
            var preloads = document.getElementsByClassName('preload')
            for (var i= 0; i < preloads.length; i++) {
                preloads[i].hidden=false
            }
            document.getElementById('loading').hidden=true;
    }

    async populateTable() {
        var table = document.getElementById("material-table");
        var oldTableBody = table.getElementsByTagName('tbody')[0];
        var newTableBody = document.createElement('tbody');
        var materialList = this.dataStore.get(MATERIAL_LIST_KEY);
        var expendable = null
        

        for(const material of materialList) {
                var row = newTableBody.insertRow(0);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);
                cell1.innerHTML = material.name;
                cell2.innerHTML = material.materialId;
                cell3.innerHTML = material.cost;
                if (material.isExpendable === false) {
                expendable = 'No'} else if (material.isExpendable === true) {
                expendable = 'Yes'};
                cell4.innerHTML = expendable;
                cell5.innerHTML = material.inventoryCount;
                var createClickHandler = function(row) {
                    return function() {
                        for (var i = 0; i < table.rows.length; i++){
                            table.rows[i].removeAttribute('class');
                        }
                        row.setAttribute('class','selectedRow')
                        //document.getElementById('view-btn').setAttribute('href', 'workerMaterialListDetail?orgId=' + new URLSearchParams(window.location.search).get('orgId'));
                    };
                };
                row.onclick = createClickHandler(row);
        }
        oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async editButton() {
        if(document.getElementById('material-id').value == "" || document.getElementById('material-name').value == "" ||
        document.getElementById('material-cost').value == 0 || document.getElementById('material-count').value == "") {
            alert("Error: please fill out all fields!")
            return;
        } else {
           await this.editMaterial();
           alert("Material updated!");
        }
    }

    async editMaterial() {
        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const materialId = document.getElementById('material-id').value;
        const cost = document.getElementById('material-cost').value;
        const inventoryCount = document.getElementById('material-count').value;
        var isExpendable = null;
        if (document.getElementById('isExpendable').value === 'Yes') {
        isExpendable = true} else if (document.getElementById('isExpendable').value === 'No') {
        isExpendable = false};
        const name = document.getElementById('material-name').value;

            await this.materialsClient.editMaterial(orgId, materialId, cost, inventoryCount, isExpendable, name)

    }

    async addButton() {
        if(document.getElementById('add-material-name').value == "" ||
        document.getElementById('add-material-cost').value == 0 || document.getElementById('add-material-count').value == "") {
            alert("Error: please fill out all fields!")
            return;
        } else {
           await this.addMaterial();
           alert("Material added!");
        }
    }

    async addMaterial() {
        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const cost = document.getElementById('add-material-cost').value;
        const inventoryCount = document.getElementById('add-material-count').value;
        var isExpendable = null;
        if (document.getElementById('add-isExpendable').value === 'Yes') {
        isExpendable = false} else if (document.getElementById('add-isExpendable').value === 'No') {
        isExpendable = true};
        const name = document.getElementById('add-material-name').value;

            await this.materialsClient.addMaterial(orgId, cost, inventoryCount, isExpendable, name)

    }

    async deleteButton() {
        if(document.getElementById('delete-id').value == "") {
            alert("Error: please fill out all fields!")
            return;
        } else {
           await this.deleteMaterial();
           alert("Material deleted!");
        }
    }

    async deleteMaterial() {
        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const materialId = document.getElementById('delete-id').value;
        await this.materialsClient.deleteMaterial(orgId, materialId)
    }



}
const main = async () => {
    const manageMaterials = new ManageMaterials();
    manageMaterials.mount();
};

window.addEventListener('DOMContentLoaded', main);