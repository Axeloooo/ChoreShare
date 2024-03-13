
import '../styles/Overlay.css'; ;

function Overlay({ content, closeOverlay }) {
    
  return (
    <div className="overlay">
        <div className="popup-window">
            <button className="close-overlay-btn" onClick={closeOverlay}>x</button>
            {content}
        </div>
    </div>
  );
}

export default Overlay;