import { Link, useLocation } from 'react-router-dom';
//import './Navbar.css';

function Navbar() {
    // useLocation permet de savoir sur quelle page on est pour surligner le menu
    const location = useLocation();

    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <h2>ChatApp</h2>
            </div>
            <ul className="navbar-links">
                <li>
                    <Link to="/planifier" className={location.pathname === '/planifier' ? 'active' : ''}>
                        Planifier une discussion
                    </Link>
                </li>
                <li>
                    <Link to="/mes-salons" className={location.pathname === '/mes-salons' ? 'active' : ''}>
                        Mes salons de discussion
                    </Link>
                </li>
                <li>
                    <Link to="/mes-invitations" className={location.pathname === '/mes-invitations' ? 'active' : ''}>
                        Mes invitations
                    </Link>
                </li>
            </ul>
        </nav>
    );
}

export default Navbar;