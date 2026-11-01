import { useEffect, useState } from "react";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/events";
const DEMO_MODE = import.meta.env.VITE_DEMO_MODE === "true";
const STORAGE_KEY = "campusconnect-demo-events";
const emptyForm = { title: "", description: "", venue: "", eventDate: "" };

function readDemoEvents() {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]");
  } catch {
    return [];
  }
}

async function requestEvents(method = "GET", id = "", payload = null) {
  if (DEMO_MODE) {
    const events = readDemoEvents();
    if (method === "GET") return events;
    if (method === "POST") {
      const created = { ...payload, id: Date.now() };
      localStorage.setItem(STORAGE_KEY, JSON.stringify([created, ...events]));
      return created;
    }
    if (method === "PUT") {
      const updated = events.map((item) => item.id === Number(id) ? { ...payload, id: Number(id) } : item);
      localStorage.setItem(STORAGE_KEY, JSON.stringify(updated));
      return updated.find((item) => item.id === Number(id));
    }
    if (method === "DELETE") {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(events.filter((item) => item.id !== Number(id))));
      return null;
    }
  }

  const response = await fetch(id ? `${API_URL}/${id}` : API_URL, {
    method,
    headers: payload ? { "Content-Type": "application/json" } : undefined,
    body: payload ? JSON.stringify(payload) : undefined,
  });
  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "The request could not be completed.");
  }
  return response.status === 204 ? null : response.json();
}

function formatDate(value) {
  return new Date(value).toLocaleString([], { dateStyle: "medium", timeStyle: "short" });
}

export default function App() {
  const [events, setEvents] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  async function loadEvents() {
    setLoading(true);
    setError("");
    try {
      setEvents(await requestEvents());
    } catch (err) {
      setError(err.message || "Could not load events. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { loadEvents(); }, []);

  function updateField(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  function startEdit(item) {
    setEditingId(item.id);
    setForm({
      title: item.title,
      description: item.description || "",
      venue: item.venue,
      eventDate: item.eventDate.slice(0, 16),
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  function resetForm() {
    setForm(emptyForm);
    setEditingId(null);
  }

  async function submitForm(event) {
    event.preventDefault();
    setSaving(true);
    setError("");
    try {
      await requestEvents(editingId ? "PUT" : "POST", editingId, form);
      resetForm();
      await loadEvents();
    } catch (err) {
      setError(err.message || "Could not save the event.");
    } finally {
      setSaving(false);
    }
  }

  async function deleteEvent(id) {
    if (!window.confirm("Delete this event?")) return;
    setError("");
    try {
      await requestEvents("DELETE", id);
      setEvents((current) => current.filter((item) => item.id !== id));
      if (editingId === id) resetForm();
    } catch (err) {
      setError(err.message || "Could not delete the event.");
    }
  }

  return (
    <main className="page-shell">
      <header className="topbar">
        <a className="brand" href="./">Campus<span>Connect</span></a>
        <span className="topbar-note">CAMPUS EVENT HUB</span>
      </header>

      <section className="hero">
        <p className="eyebrow">YOUR CAMPUS. YOUR COMMUNITY.</p>
        <h1>Make campus moments <span>happen.</span></h1>
        <p className="hero-copy">Create events, share the details, and keep your campus community in sync.</p>
        {DEMO_MODE && <p className="demo-note">LIVE DEMO · Data is saved in this browser only</p>}
      </section>

      {error && <div className="alert" role="alert">{error}</div>}

      <section className="content-grid">
        <form className="panel event-form" onSubmit={submitForm}>
          <div className="panel-heading">
            <div><p className="eyebrow">EVENT MANAGER</p><h2>{editingId ? "Update event" : "Create an event"}</h2></div>
            <span className="heading-icon">✦</span>
          </div>
          <label>Event title<input name="title" value={form.title} onChange={updateField} maxLength="120" placeholder="e.g. Annual Tech Fest" required /></label>
          <label>Venue<input name="venue" value={form.venue} onChange={updateField} maxLength="160" placeholder="e.g. Main Auditorium" required /></label>
          <label>Date and time<input name="eventDate" type="datetime-local" value={form.eventDate} onChange={updateField} required /></label>
          <label>Description <span className="optional">(optional)</span><textarea name="description" value={form.description} onChange={updateField} maxLength="2000" rows="4" placeholder="What should attendees know?" /></label>
          <div className="form-actions">
            <button className="primary-button" disabled={saving}>{saving ? "Saving..." : editingId ? "Save changes" : "Publish event"}</button>
            {editingId && <button className="quiet-button" type="button" onClick={resetForm}>Cancel</button>}
          </div>
        </form>

        <section className="events-section">
          <div className="section-heading">
            <div><p className="eyebrow">WHAT'S HAPPENING</p><h2>Campus events</h2></div>
            <span className="count-pill">{events.length} {events.length === 1 ? "EVENT" : "EVENTS"}</span>
          </div>
          {loading ? <div className="empty-state">Loading events...</div> : events.length === 0 ? (
            <div className="empty-state"><span className="empty-icon">✧</span><h3>No events yet</h3><p>Your next campus highlight starts with the first event.</p></div>
          ) : (
            <div className="event-list">
              {events.map((item) => (
                <article className="event-card" key={item.id}>
                  <div className="event-card-top"><span className="event-tag">CAMPUS EVENT</span><span className="event-date">{formatDate(item.eventDate)}</span></div>
                  <h3>{item.title}</h3>
                  <p className="event-venue">⌖ {item.venue}</p>
                  {item.description && <p className="event-description">{item.description}</p>}
                  <div className="card-actions"><button className="quiet-button" onClick={() => startEdit(item)}>Edit</button><button className="delete-button" onClick={() => deleteEvent(item.id)}>Delete</button></div>
                </article>
              ))}
            </div>
          )}
        </section>
      </section>
      <footer>CampusConnect <span>·</span> Built to bring campus communities together.</footer>
    </main>
  );
}
