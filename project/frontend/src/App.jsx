import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';

function App() {
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
                            <p>Connecté : <strong>Utilisateur</strong></p>
                        </div>
                    </aside>

                    {/* Colonne de droite (Contenu) */}
                    <main className="main-content">
                        <Routes>
                            <Route path="/" element={<Navigate to="/mes-salons" replace />} />

                            <Route path="/planifier" element={
                                <div className="page-content">
                                    {/* TODO */}
                                    <h2>Planifier une discussion</h2>
                                    <p>Formulaire à venir (Titre, description, date, horaire, durée).</p>
                                </div>
                            } />

                            <Route path="/mes-salons" element={
                                <div className="page-content">
                                    {/* TODO */}
                                    <h2>Mes salons de discussion</h2>
                                    <p>Tableau paginé des salons dont je suis propriétaire à venir.</p>
                                </div>
                            } />

                            <Route path="/mes-invitations" element={
                                <div className="page-content">
                                    {/* TODO */}
                                    <h2>Mes invitations</h2>
                                    <p>Tableau paginé des salons où je suis invité à venir.</p>
                                </div>
                            } />
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