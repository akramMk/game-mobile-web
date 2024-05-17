// Importation d'Express en tant que module ES
import express from 'express';
import path from 'path';
import gameRouter from './routes/gameRoutes.js';
import adminRouter from  './routes/adminRoutes.js';
import cors from 'cors';

// const express = require('express')
const app = express()
const port = 3376

// Configuration de CORS pour autoriser seulement http://localhost:3376/
const corsOptions = {
	origin: ['http://localhost:3376','http://192.168.75.14:3376','https://192.168.75.14/game'],
};
// Utilisation du middleware CORS avec les options spécifiées
app.use(cors(corsOptions));
app.use(express.json());
// Utilisation du middleware express.static pour servir les fichiers statiques
// du répertoire 'public'
app.use('/static', express.static(path.join('./', 'public')));

app.get('/', (req, res) => {
	// res.send('Hello World!')
	res.sendFile('indexMedini.html', { root: path.join('./', 'public') });
})

app.get('/admin', (req, res) => {
	// res.send('Hello World!')
	res.sendFile('admin.html', { root: path.join('./', 'public') });
})

// Middleware pour le fonctionnement du jeu
app.use('/api', gameRouter);

// Middleware pour l'interface d'administration
app.use('/admin', adminRouter);

// Middleware de gestion des erreurs 404
app.use((req, res) => {
	res.status(404).send("Désolé, la page que vous recherchez est introuvable.");
});

app.listen(port, () => {
	console.log(`Listening on port ${port}`)
})
