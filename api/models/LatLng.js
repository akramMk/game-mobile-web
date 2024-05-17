export default class LatLng {
	constructor(lat, lng) {
		this.lat = lat;
		this.lng = lng;
	}
	getLat() {
		return this.lat;
	}
	getLng() {
		return this.lng;
	}
	setLat(lat) {
		this.lat = lat;
	}
	setLng(lng) {
		this.lng = lng;
	}
	distanceTo(latLng) {
		const dx = this.lat - latLng.getLat();
		const dy = this.lng - latLng.getLng();
		return Math.sqrt(dx * dx + dy * dy);
	}
}