import express from 'express';
import GeoRessource from '../models/GeoRessource.js';
import {OperationType} from "../models/OperationType.js";
import ZRR from "../models/ZRR.js";
import LatLng from "../models/LatLng.js";
import axios from "axios";

const gameRouter = express.Router();

// Route pour obtenir toutes les ressources
gameRouter.get('/resources', validateUser, (req, res) => {
	res.status(200).send(
		GeoRessource.getAllRessources()
	);
});

// Un villageois peut éliminer un pirate
// Un pirate peut transformer un villageois
// Un villageois ou un pirate peuvent ramasser une fiole
gameRouter.post('/resources/:id', validateUser, (req, res) => {

	const resourceId = parseInt(req.params.id);
	const operationType = req.body.operationType;
	let status_code = 0;

	switch (operationType) {
	case OperationType.GRAB_POTION:
		// Récupérer une fiole
		status_code = GeoRessource.grabPotion(resourceId);
		break;
	case OperationType.TERMINATE_PIRATE:
		// Eliminer un pirate
		status_code = GeoRessource.terminatePirate(resourceId);
		break;
	case OperationType.TURN_VILLAGER_INTO_PIRATE:
		// Transformer un villageois
		status_code = GeoRessource.turnVillagerIntoPirate(resourceId);
		break;
	default:
		res.status(400).json({
			message: "Opération non autorisée"
		});
		return;
	}

	if (status_code === 404) {
		res.status(404).json({
			message: "Ressource non trouvée"
		});
		return;
	}
	if (status_code === 400) {
		res.status(400).json({
			message: "Opération non autorisée"
		});
		return;
	}
	if (status_code === 403) {
		res.status(403).json({
			message: "Utilisateur loin de la cible"
		});
		return;
	}

	res.status(204).json({
		message: "Opération effectuée avec succès"
	});
	//console.log(GeoRessource.getAllRessources());
});

// Mise à jour de position
gameRouter.put('/resources/:id/position', validateUser, (req, res) => {
	const resourceId = parseInt(req.params.id);
	const lat = req.body.lat;
	const lng = req.body.lng;

	if (!GeoRessource.verifyId(resourceId)) {
		res.status(404).json({
			message: "Ressource non trouvée"
		});
		return;
	}

	if (!GeoRessource.modifyPosition(resourceId, new LatLng(lat, lng))) {
		console.log(resourceId);
		res.status(400).json({
			message: "Position en dehors de la ZRR"
		});
		return;
	}

	res.status(204);
	//console.log(GeoRessource.getAllRessources());
});

// Récupération de la limite de la zrr
gameRouter.get('/zrr', validateUser, (req, res) => {

	res.status(200).json({
		message: "Limite de la ZRR récupérée avec succès",
		ressources: {
			limit_NO: ZRR.limit_NO,
			limit_NE: ZRR.limit_NE,
			limit_SE: ZRR.limit_SE,
			limit_SO: ZRR.limit_SO
		}
	});
})

async function validateUser(req, res, next) {
	const response = await axios.get(
		'https://192.168.75.14/api/users/authenticate?Authorization='
			+ req.headers.authorization + '&Origin=' + req.headers.origin);
	if (response.status !== 204) {
		res.status(401).send("Utilisateur non autorisé");
		return;
	}
	next();
}

export default gameRouter;
