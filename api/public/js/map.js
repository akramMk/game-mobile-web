// initialisation de la map
const lat = 45.782, lng = 4.8656, zoom = 19;
/* global L */
let mymap = L.map('map', {
	center: [lat, lng],
	zoom: zoom
});

// Initialisation de la map
function initMap() {
	// Création d'un "tile layer" (permet l'affichage sur la carte)
	L.tileLayer('https://api.mapbox.com/v4/mapbox.satellite/{z}/{x}/{y}@2x.jpg90?access_token=pk.eyJ1IjoieGFkZXMxMDExNCIsImEiOiJjbGZoZTFvbTYwM29sM3ByMGo3Z3Mya3dhIn0.df9VnZ0zo7sdcqGNbfrAzQ', {
		maxZoom: 21,
		minZoom: 1,
		attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
			'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
		id: 'mapbox/streets-v11',
		tileSize: 512,
		zoomOffset: -1,
		accessToken: 'pk.eyJ1IjoibTFpZjEzIiwiYSI6ImNqczBubmhyajFnMnY0YWx4c2FwMmRtbm4ifQ.O6W7HeTW3UvOVgjCiPrdsA'
	}).addTo(mymap);

	// Ajout d'un marker
	L.marker([45.78207, 4.86559]).addTo(mymap).bindPopup('Entrée du bâtiment<br>Nautibus.').openPopup();

	// Clic sur la carte
	mymap.on('click', e => {
		updateMap([e.latlng.lat, e.latlng.lng], mymap.getZoom());
	});

	return mymap;
}

// Mise à jour de la map
function updateMap(latlng, zoom) {
	// Affichage à la nouvelle position
	mymap.setView(latlng, zoom);

	// La fonction de validation du formulaire renvoie false pour bloquer le rechargement de la page.
	return false;
}

export { updateMap };
export default initMap;