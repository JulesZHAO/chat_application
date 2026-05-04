import { Link, useLocation } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
    // useLocation permet de savoir sur quelle page on est pour surligner le menu
    const location = useLocation();

    return (
        <header>
            {/* Le grand bandeau du titre */}
            <div className="top-banner">
                <h1>Salon de discussion</h1>
            </div>

            {/* La barre de navigation */}
            <nav className="navbar-dark">
                <ul className="navbar-links">
                    <li>
                        <Link to="/planifier" className={location.pathname === '/planifier' ? 'active' : ''}>
                            PLANIFIER UNE DISCUSSION
                        </Link>
                    </li>
                    <li>
                        <Link to="/mes-salons" className={location.pathname === '/mes-salons' ? 'active' : ''}>
                            MES SALONS DE DISCUSSION
                        </Link>
                    </li>
                    <li>
                        <Link to="/mes-invitations" className={location.pathname === '/mes-invitations' ? 'active' : ''}>
                            MES INVITATIONS
                        </Link>
                    </li>
                </ul>
            </nav>
        </header>
    );
}

export default Navbar;