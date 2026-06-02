import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import MesSalons from "./components/MesSalons";
import MesInvitations from "./components/MesInvitations";
import Planifier from "./components/Planifier";
import Chat from "./components/Chat";
import { useEffect, useState } from "react";

function App() {
    const [utilisateur, setUtilisateur] = useState(null);

    useEffect(() => {
        fetch("/moi")
            .then(res => res.ok ? res.json() : null)
            .then(data => setUtilisateur(data))
            .catch(() => setUtilisateur(null));
    }, []);

    return (
        <Router>
            <div className="app-container">
                <Navbar />

                {/* Conteneur principal séparé en 2 colonnes */}
                <div className="content-wrap">

                    {/* Colonne de gauche */}
                    <aside className="sidebar">
                        <div className="info-card">
                            <h4>Informations</h4>
                            <hr />
                            <p>Connecté : <strong>{utilisateur ? `${utilisateur.prenom} ${utilisateur.nom}` : "Non connecté"}</strong></p>
                            {utilisateur?.email && <p className="sidebar-muted">{utilisateur.email}</p>}
                        </div>
                    </aside>

                    {/* Colonne de droite (Contenu) */}
                    <main className="main-content">
                        <Routes>
                            <Route path="/" element={<Navigate to="/mes-salons" replace />} />
                            <Route path="/planifier" element={<Planifier />} />
                            <Route path="/mes-salons" element={<MesSalons />}  />
                            <Route path="/mes-invitations" element={<MesInvitations />} />
                            <Route path="/chat/:canalId" element={<Chat />} />
                        </Routes>
                    </main>
                </div>

                <footer className="admin-footer">
                    <p>SR03 Salon de discussion 2026 - Gaspard PETRI - Zhenyu ZHAO</p>
                </footer>
            </div>
        </Router>
    );
}

export default App;