import { useEffect, useState} from "react";

function MesInvitations() {
    const [invitations, setInvitations] = useState([]);
    useEffect(() => {
        fetch("/canaux/invite/1")
            .then(res => res.json())
            .then(data => setInvitations(data))
    }, [])
    return (
        <div className="page-content">
            <h2>Mes invitations</h2>

            {invitations.length === 0 ? (
                <p>Aucune invitation pour le moment.</p>
            ) : (
                invitations.map(invitation => (
                    <div key={invitation.id} className="canal-card">
                        <div>
                            <h3>{invitation.titre}</h3>
                            <p>{invitation.description}</p>
                        </div>
                    </div>
                ))
            )}
        </div>
    )
};

export default MesInvitations;