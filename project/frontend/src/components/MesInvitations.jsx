import { useEffect, useState} from "react";

function MesInvitations() {
    const [invitations, setInvitations] = useState([]);
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        fetch("/moi").then(res => res.json()).then(u => setUserId(u.id))
    }, []);

    useEffect(() => {
        if (!userId) return;
        fetch(`/canaux/invite/${userId}`)
            .then(res => res.json())
            .then(data => setInvitations(data))
    }, [userId])
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
                            <button onClick={() => window.open(`/chat/${invitation.id}`, '_blank')}>
                                Rejoindre le chat
                            </button>
                        </div>
                    </div>
                ))
            )}
        </div>
    )
}

export default MesInvitations;