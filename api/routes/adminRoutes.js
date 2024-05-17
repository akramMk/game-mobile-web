import express from 'express';
import ZRR from '../models/ZRR.js';
import TTL from '../datas/TTL.js';
import GeoRessource from '../models/GeoRessource.js';
import LatLng from "../models/LatLng.js";
import {Roles} from "../models/Roles.js";
import axios from "axios";

const adminRouter = express.Router()

adminRouter.post('/zrr', validateAdmin, (req, res) => {

	if (req.body.limit_NE === null || req.body.limit_NW === null || req.body.limit_SE === null || req.body.limit_SW === null) {
		res.status(405).json({
			message: "Veuillez renseigner les limites de la ZRR, NortheEast et SouthWest"
		});
	}
	const NW = req.body.limit_NW;
	const NE = req.body.limit_NE;
	const SW = req.body.limit_SW;
	const SE = req.body.limit_SE;

	ZRR.limit_NO = new LatLng(NW[0], NW[1]);
	ZRR.limit_NE = new LatLng(NE[0], NE[1]);
	ZRR.limit_SO = new LatLng(SW[0], SW[1]);
	ZRR.limit_SE = new LatLng(SE[0], SE[1]);

	res.status(200).json({
		message: "Limites de la ZRR fixées avec succès."
	})
	//console.log(ZRR.limit_NE, ZRR.limit_NO, ZRR.limit_SE, ZRR.limit_SO)
});

// préciser la TTL initial pour les fioles (par défault 1 minute)
adminRouter.post('/ttl', validateAdmin, (req, res) => {

	if (req.body.ttl === null) {
		res.status(405).json({
			message: "Veuillez renseigner la durée de vie des fioles."
		});
	}

	TTL.ttl = req.body.ttl;

	//console.log(TTL.ttl);
	res.status(200).json({
		message: "Durée de vie des fioles fixée avec succès"
	})
});

// Déclencher l'apparition d'une fiole
adminRouter.post('/fiole', validateAdmin, (req, res) => {

	if (req.body.id === null || req.body.position === null || req.body.role === null) {
		res.status(405).json({
			message: "Veuillez renseigner les informations nécessaires pour l'apparition d'une fiole."
		});
	}
	const ttl = TTL.ttl;
	const lat = req.body.lat;
	const lng = req.body.lng;

	const fiole = {
		id: GeoRessource.getID(),
		position: new LatLng(lat, lng),
		role: Roles.FIOLE,
		ttl: ttl
	}
	GeoRessource.add(fiole);
	// Requeter le serveur pour déclencher l'apparition d'une fiole
	res.status(200).json({
		message: "Fiole apparue avec succès"
	})
	//console.log(GeoRessource.getAllRessources());
});
async function validateAdmin(req, res, next) {
	const response = await axios.get(
		'https://192.168.75.14/api/users/authenticate?Authorization='
		+ req.headers.authorization + '&Origin=' + req.headers.origin);
	if (response.status !== 204) {
		res.status(401).send("Utilisateur non autorisé");
		return;
	}
	next();
}

export default adminRouter;