import TTL from '../datas/TTL.js'
import LatLng from "./LatLng.js";
import {Roles} from "./Roles.js";



export default class GeoRessource {
	static ressourceList = [
		{id : 1, role : Roles.VILLAGEOIS, position: new LatLng(0.2154, 60.1564), ttl: TTL.ttl, potions: 0, terminated:0},
		{id : 2, role: Roles.PIRATE, position: new LatLng(0.2154, 60.1564), ttl: TTL.ttl, potions: 0, turned: 0},
		{id : 3, role: Roles.FIOLE, position: new LatLng(0.2154, 60.1564), ttl: TTL.ttl},
	];
	static ressourceNumber = 0;
	static add(ressource) {
		this.ressourceList.push(ressource);
		this.ressourceNumber++;
	}
	static getAllRessources() {
		return this.ressourceList;
	}
	static modifyPosition(id, position) {
		let found = false;
		this.ressourceList.forEach(ressource => {
			if (ressource.id === id) {
				ressource.position = position;
				found = true;
			}
		});
		return found;
	}
	static verifyId(id) {
		let found = false;
		this.ressourceList.forEach(ressource => {
			if (ressource.id === id) {
				found = true;
			}
		});
		return found;
	}

	static getPositionWithId(id) {
		for (const ressource of this.ressourceList) {
			if (ressource.id === id) {
				return ressource.position;
			}
		}
		return null;
	}

	static grabPotion(id) {
		if (this.getRole(id) !== Roles.VILLAGEOIS && this.getRole(id) !== Roles.PIRATE) {
			return 404;
		}
		const playerPosition = this.getPositionWithId(id);
		// On itère sur les fiole
		for (let i = 0; i < this.ressourceList.length; i++) {
			const ressource = this.ressourceList[i];
			if (ressource.role === Roles.FIOLE) {
				// On vérifie si la distance est moins de 5m
				const distance = playerPosition.distanceTo(ressource.position);
				if (distance < 5) {
					this.ressourceList = this.ressourceList.filter(item => item !== ressource);
					for (let j = 0; j < this.ressourceList.length; j++) {
						const ressource = this.ressourceList[j];
						if (ressource.id === id) {
							ressource.ttl += TTL.ttl;
							ressource.potions += 1;
						}
					}
					return 204;
				} else {
					return 403;
				}
			}
		}
		return 400;
	}

	static getID() {
		return this.ressourceList.length + 1;
	}
	static getRole(id) {
		for (let i = 0; i < this.ressourceList.length; i++) {
			const ressource = this.ressourceList[i];
			if (ressource.id === id) {
				return ressource.role;
			}
		}
	}
	static terminatePirate(id) {
		if (this.getRole(id) !== Roles.VILLAGEOIS) {
			return 404;
		}
		const playerPosition = this.getPositionWithId(id);
		for (let i = 0; i < this.ressourceList.length; i++) {
			const ressource = this.ressourceList[i];
			if (ressource.role === Roles.PIRATE) {
				const distance = playerPosition.distanceTo(ressource.position);
				if (distance < 5) {
					this.ressourceList = this.ressourceList.filter(item => item !== ressource);
					this.ressourceList.forEach(ressource => {
						if (ressource.id === id) {
							ressource.terminated += 1;
						}
					});
					return 204;
				} else {
					return 403;
				}
			}
		}
		return 400;
	}

	static turnVillagerIntoPirate(id) {
		if (this.getRole(id) !== Roles.PIRATE) {
			return 404;
		}
		const playerPosition = this.getPositionWithId(id);
		for (let i = 0; i < this.ressourceList.length; i++) {
			const ressource = this.ressourceList[i];
			if (ressource.role === Roles.VILLAGEOIS) {
				const distance = playerPosition.distanceTo(ressource.position);
				if (distance < 5) {
					ressource.role = Roles.PIRATE;
					this.ressourceList.forEach(res => {
						if (res.id === id) {
							res.turned += 1;
						}
					});
					return 204;
				} else {
					return 403;
				}
			}
		}
		return 400;
	}
}

