// Initialisation

// eslint-disable-next-line no-unused-vars
import {updateMap} from "./map.js";
import apiPath from './apiPath.js'; // Importez la variable globale
function initListeners(mymap) {
	let bounds = mymap.getBounds();
	console.log("TODO: add more event listeners...");

	mymap.on('moveend', () => {const center = mymap.getCenter();const zoom = mymap.getZoom();updateForm(center.lat, center.lng, zoom);bounds = mymap.getBounds();});

	// Ajouter des gestionnaires d'événements pour détecter les changements de valeur dans les champs de formulaire
	document.getElementById("lat").addEventListener("input", () => {
		const lat = parseFloat(document.getElementById("lat").value);
		const lng = parseFloat(document.getElementById("lon").value);
		const zoom = parseInt(document.getElementById("zoom").value);
		updateMap([lat, lng], zoom);
	});

	document.getElementById("lon").addEventListener("input", () => {
		const lat = parseFloat(document.getElementById("lat").value);
		const lng = parseFloat(document.getElementById("lon").value);
		const zoom = parseInt(document.getElementById("zoom").value);
		updateMap([lat, lng], zoom);
	});

	document.getElementById("zoom").addEventListener("input", () => {
		const lat = parseFloat(document.getElementById("lat").value);
		const lng = parseFloat(document.getElementById("lon").value);
		const zoom = parseInt(document.getElementById("zoom").value);
		updateMap([lat, lng], zoom);
	});

	document.getElementById("setZrrButton").addEventListener("click", () => {
		setZrr(bounds);
	});

	document.getElementById("sendZrrButton").addEventListener("click", () => {
		sendZrr();
	});

	document.getElementById("setTtlButton").addEventListener("click", () => {
		setTtl();
	});
}

// MàJ des inputs du formulaire
function updateLatValue(lat) {
	document.getElementById("lat").value = lat;
}

function updateLonValue(lng) {
	document.getElementById("lon").value = lng;
}

function updateZoomValue(zoom) {
	document.getElementById("zoom").value = zoom;
}

function updateForm(lat, lng, zoom) {updateLatValue(lat);updateLonValue(lng);updateZoomValue(zoom);}

// eslint-disable-next-line no-unused-vars
function setZrr(bounds) {
	// Récupérer les limites de la carte

	// Mettre à jour les champs du formulaire avec les coordonnées des coins de la zone définie
	const lat1 = bounds.getNorthWest().lat;
	const lng1 = bounds.getNorthWest().lng;
	const lat2 = bounds.getNorthEast().lat;
	const lng2 = bounds.getNorthEast().lng;
	const lat3 = bounds.getSouthWest().lat;
	const lng3 = bounds.getSouthWest().lng;
	const lat4 = bounds.getSouthEast().lat;
	const lng4 = bounds.getSouthEast().lng;

	document.getElementById("lat1").value = lat1;
	document.getElementById("lon1").value = lng1;
	document.getElementById("lat2").value = lat2;
	document.getElementById("lon2").value = lng2;
	document.getElementById("lat3").value = lat3;
	document.getElementById("lon3").value = lng3;
	document.getElementById("lat4").value = lat4;
	document.getElementById("lon4").value = lng4;
	console.log("TODO: update input values...");
}

// Requêtes asynchrones
async function sendZrr() {
	console.log("TODO: send fetch request...");

	// Récupérer le token JWT du localStorage
	/* eslint-env browser */
	const token = localStorage.getItem('token');

	// Vérifier si le token est présent
	if (!token) {
		console.error("Token non trouvé dans le localStorage");
		return;
	}

	const lat1 = document.getElementById("lat1").value;
	const lon1 = document.getElementById("lon1").value;
	const lat2 = document.getElementById("lat2").value;
	const lon2 = document.getElementById("lon2").value;
	const lat3 = document.getElementById("lat3").value;
	const lon3 = document.getElementById("lon3").value;
	const lat4 = document.getElementById("lat4").value;
	const lon4 = document.getElementById("lon4").value;

	// Paramètres de la requête POST
	const params = {
		limit_NW: [lat1,lon1],
		limit_NE: [lat2,lon2],
		limit_SW: [lat3,lon3],
		limit_SE: [lat4,lon4],
	};
	//
	// Configuration de la requête fetch
	const fetchOptions = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		},
		body: JSON.stringify(params)
	};
	const url = `${apiPath}/admin/zrr`

	// Effectuer la requête
	await fetch(url, fetchOptions)
		.then(response => {
			// Vérifier le statut de la réponse
			if (response.status === 200){
				console.log("sendZrr avec success !!")
			}
			else{
				throw new Error('Erreur lors de la requête : ' + response.statusText);
			}
		})
		.catch(error => {
			console.error('Erreur :', error);
			// Gérer les erreurs, par exemple, afficher un message d'erreur à l'utilisateur
		});
}

async function setTtl() {
	console.log("TODO: send fetch request...");
	// Récupérer le token JWT du localStorage
	/* eslint-env browser */
	const token = localStorage.getItem('token');

	// Vérifier si le token est présent
	if (!token) {
		console.error("Token non trouvé dans le localStorage");
		return;
	}

	const ttl = document.getElementById("ttl").value;

	// Paramètres de la requête POST
	const params = {
		ttl: ttl,
	};
	//
	// Configuration de la requête fetch
	const fetchOptions2 = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		},
		body: JSON.stringify(params)
	};

	const url = `${apiPath}/admin/ttl`
	// Effectuer la requête
	await fetch(url, fetchOptions2)
		.then(response => {
			// Vérifier le statut de la réponse
			if (response.status === 200){
				console.log("setTtl avec success !!")
			}
			else{
				throw new Error('Erreur lors de la requête : ' + response.statusText);
			}
		})
		.catch(error => {
			console.error('Erreur :', error);
			// Gérer les erreurs, par exemple, afficher un message d'erreur à l'utilisateur
		});
}

export { updateLatValue, updateLonValue, updateZoomValue };
export default initListeners;