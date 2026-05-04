import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';

function App() {
    return (
        <Router>
            <div className="app-container">
                <Navbar />

                <div className="content-wrap" style={{ padding: '20px' }}>
                    <Routes>
                        {/* Redirection par défaut si l'utilisateur arrive sur la racine */}
                        <Route path="/" element={<Navigate to="/mes-salons" replace />} />

                        <Route path="/planifier" element={
                            <div>
                                {/* TODO */}
                                <h2>Planifier une discussion</h2>
                                <p>Formulaire à venir (Titre, description, date, horaire, durée).</p>
                            </div>
                        } />

                        <Route path="/mes-salons" element={
                            <div>
                                {/* TODO */}
                                <h2>Mes salons de discussion</h2>
                                <p>Tableau paginé des salons dont je suis propriétaire</p>
                            </div>
                        } />

                        <Route path="/mes-invitations" element={
                            <div>
                                {/* TODO */}
                                <h2>Mes invitations</h2>
                                <p>Tableau paginé des salons où je suis invité</p>
                            </div>
                        } />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;