import React, { useState, useEffect, useMemo } from 'react';
import styled from 'styled-components';
import { format, addDays } from 'date-fns';
import { Calendar as CalendarIcon, Clock, User as UserIcon, X, CalendarClock, CheckCircle } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { doctorService, appointmentService } from '../../services/api';
import { PROCEDURE_TYPES } from '../../config/constants';
import { useTranslation } from 'react-i18next';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const Header = styled.div`
  h1 {
    font-size: 2rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.5rem;
  }
  p {
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

const TabsContainer = styled.div`
  display: flex;
  gap: 1rem;
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  padding-bottom: 0.5rem;
`;

const Tab = styled.button`
  background: none;
  border: none;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  font-weight: 600;
  color: ${({ active, theme }) => (active ? theme.colors.primary : theme.colors.textMuted)};
  border-bottom: 2px solid ${({ active, theme }) => (active ? theme.colors.primary : 'transparent')};
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    color: ${({ theme }) => theme.colors.primary};
  }
`;

const AppointmentsList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const AppointmentCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;

  @media (max-width: 768px) {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
`;

const InfoSection = styled.div`
  display: flex;
  gap: 1.5rem;
  align-items: center;
`;

const Avatar = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: ${({ theme }) => theme.colors.surfaceHover};
  display: flex;
  align-items: center;
  justify-content: center;
  color: ${({ theme }) => theme.colors.textMuted};
`;

const Details = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.25rem;

  .name {
    font-weight: 600;
    font-size: 1.1rem;
    color: ${({ theme }) => theme.colors.text};
  }

  .time {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.9rem;
  }
`;

const ActionsSection = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;
`;

const StatusBadge = styled.span`
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.85rem;
  font-weight: 600;
  text-transform: capitalize;
  
  ${({ status }) => {
    if (status === 'COMPLETED') return `background: #dcfce7; color: #166534;`;
    if (status === 'CANCELLED') return `background: #fee2e2; color: #991b1b;`;
    return `background: #eff6ff; color: #1e3a8a;`;
  }}
`;

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContent = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  padding: 2rem;
  border-radius: ${({ theme }) => theme.radii.lg};
  width: 95%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;

  h3 {
    margin: 0;
    color: ${({ theme }) => theme.colors.text};
  }
`;

const DateSelector = styled.div`
  display: flex;
  gap: 0.75rem;
  overflow-x: auto;
  padding-bottom: 1rem;
  
  &::-webkit-scrollbar {
    height: 6px;
  }
`;

const DateCard = styled.div`
  min-width: 75px;
  background: ${({ selected, theme }) => selected ? theme.colors.primary : theme.colors.surface};
  border: 1px solid ${({ selected, theme }) => selected ? theme.colors.primary : theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  padding: 0.75rem;
  text-align: center;
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};
  color: ${({ selected, theme }) => selected ? '#fff' : theme.colors.text};

  &:hover {
    border-color: ${({ theme }) => theme.colors.primary};
  }

  .day { font-size: 0.8rem; margin-bottom: 0.25rem; opacity: 0.9; }
  .date { font-size: 1.1rem; font-weight: 700; }
`;

const TimeGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.75rem;
  max-height: 220px;
  overflow-y: auto;
  padding-right: 0.5rem;
`;

const TimeSlot = styled.button`
  padding: 0.75rem;
  border-radius: ${({ theme }) => theme.radii.md};
  font-weight: 600;
  font-size: 0.9rem;
  transition: ${({ theme }) => theme.transitions.default};
  
  background: ${({ selected, theme }) => selected ? theme.colors.primary : theme.colors.surface};
  color: ${({ selected, theme }) => selected ? '#fff' : theme.colors.text};
  border: 1px solid ${({ selected, theme }) => selected ? theme.colors.primary : theme.colors.border};
  cursor: pointer;
  
  &:hover {
    border-color: ${({ theme }) => theme.colors.primary};
  }
`;



const parseBackendDate = (dt) => {
  if (!dt) return null;
  if (Array.isArray(dt)) {
    const [y, m, d, h, min] = dt;
    return new Date(y, m - 1, d, h, min);
  }
  return new Date(dt);
};

const DoctorSchedule = () => {
  const { t } = useTranslation();
  const doctor = JSON.parse(localStorage.getItem('user') || '{}');
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('upcoming');
  
  const upcomingDates = useMemo(() => Array.from({ length: 30 }, (_, i) => addDays(new Date(), i)), []);

  // Reschedule Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  
  const [selectedRescheduleDate, setSelectedRescheduleDate] = useState(upcomingDates[0]);
  const [selectedRescheduleTime, setSelectedRescheduleTime] = useState(null);
  
  const fetchAppointments = async () => {
    try {
      setLoading(true);
      const res = await doctorService.getMe();
      if (res.data && res.data.appointments) {
        setAppointments(res.data.appointments);
      }
    } catch (error) {
      console.error('Failed to fetch schedule', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  const handleCancel = async (id) => {
    if (window.confirm('Are you sure you want to cancel this appointment?')) {
      try {
        await appointmentService.updateStatus(id, { updatedStatus: 'CANCELLED' });
        fetchAppointments();
      } catch (err) {
        console.error('Failed to cancel appointment', err);
        alert('Failed to cancel appointment.');
      }
    }
  };

  const handleComplete = async (id) => {
    try {
      await appointmentService.updateStatus(id, { updatedStatus: 'COMPLETED' });
      fetchAppointments();
    } catch (err) {
      console.error('Failed to complete appointment', err);
      alert('Failed to mark appointment as completed.');
    }
  };

  const openRescheduleModal = (appointment) => {
    setSelectedAppointment(appointment);
    if (appointment.appointmentDateTime) {
      const appDate = parseBackendDate(appointment.appointmentDateTime);
      if (appDate) {
        // Try to find if the date is within our 14 days array
        const foundDate = upcomingDates.find(d => format(d, 'yyyy-MM-dd') === format(appDate, 'yyyy-MM-dd'));
        if (foundDate) setSelectedRescheduleDate(foundDate);
        
        setSelectedRescheduleTime({
          time: format(appDate, 'HH:mm'),
          label: format(appDate, 'hh:mm a')
        });
      }
    } else {
      setSelectedRescheduleDate(upcomingDates[0]);
      setSelectedRescheduleTime(null);
    }
    setIsModalOpen(true);
  };

  const submitReschedule = async () => {
    if (!selectedRescheduleTime) return;
    try {
      const dateString = format(selectedRescheduleDate, 'yyyy-MM-dd');
      const dateTimeIso = `${dateString}T${selectedRescheduleTime.time}:00`;

      await appointmentService.updateDateTime(selectedAppointment.id, { 
        dateTime: dateTimeIso,
        duration: selectedAppointment.duration || 30
      });
      setIsModalOpen(false);
      fetchAppointments();
    } catch (err) {
      console.error('Failed to reschedule', err);
      alert('Failed to reschedule appointment.');
    }
  };

  const timeSlots = useMemo(() => {
    const slots = [];
    
    // Create a Set of "HH:mm" strings representing booked times for this specific date
    const bookedTimes = new Set();
    if (appointments && Array.isArray(appointments)) {
      appointments.forEach(app => {
        // Don't block the slot of the appointment we're currently rescheduling
        if (selectedAppointment && app.id === selectedAppointment.id) return;
        if (app.appointmentStatus === 'REJECTED' || app.appointmentStatus === 'CANCELLED') return;
        
        const appDate = parseBackendDate(app.appointmentDateTime);
        if (appDate && format(appDate, 'yyyy-MM-dd') === format(selectedRescheduleDate, 'yyyy-MM-dd')) {
          bookedTimes.add(format(appDate, 'HH:mm'));
        }
      });
    }

    // Determine specialty interval
    const spec = PROCEDURE_TYPES.find(p => 
      doctor.specialization && (
        p.category.toUpperCase() === doctor.specialization.toUpperCase() || 
        p.category.toUpperCase().replace(/\s+/g, '_') === doctor.specialization.toUpperCase()
      )
    );
    
    let maxDuration = 30; // default safe fallback
    if (spec && spec.subtypes && spec.subtypes.length > 0) {
      maxDuration = Math.max(...spec.subtypes.map(s => s.duration));
    }
    const interval = maxDuration + 15; // + 15 mins break

    const startHour = 9;
    const endHour = 17;

    let currentMin = startHour * 60; // 9:00 in minutes
    const endMin = endHour * 60; // 17:00 in minutes

    while (currentMin + interval <= endMin) {
      const h = Math.floor(currentMin / 60);
      const m = currentMin % 60;
      
      const timeStr = `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
      const d = new Date();
      d.setHours(h, m, 0, 0);
      
      slots.push({
        time: timeStr,
        label: format(d, 'hh:mm a'),
        available: !bookedTimes.has(timeStr)
      });
      
      currentMin += interval;
    }
    
    return slots;
  }, [selectedRescheduleDate, appointments, selectedAppointment, doctor.specialization]);

  const filteredAppointments = appointments.filter(app => {
    const status = app.appointmentStatus || 'CREATED';
    if (activeTab === 'upcoming') return status === 'CREATED' || status === 'CONFIRMED';
    if (activeTab === 'past') return status === 'COMPLETED';
    if (activeTab === 'cancelled') return status === 'CANCELLED';
    return false;
  }).sort((a, b) => {
    const dateA = parseBackendDate(a.appointmentDateTime) || new Date(0);
    const dateB = parseBackendDate(b.appointmentDateTime) || new Date(0);
    return dateA - dateB;
  });

  if (loading) return <div style={{ padding: '2rem' }}>Loading schedule...</div>;

  return (
    <PageContainer>
      <Header>
        <h1>Manage Schedule</h1>
        <p>View and manage your upcoming patient appointments.</p>
      </Header>

      <TabsContainer>
        <Tab active={activeTab === 'upcoming'} onClick={() => setActiveTab('upcoming')}>Upcoming</Tab>
        <Tab active={activeTab === 'past'} onClick={() => setActiveTab('past')}>Completed</Tab>
        <Tab active={activeTab === 'cancelled'} onClick={() => setActiveTab('cancelled')}>Cancelled</Tab>
      </TabsContainer>

      <AppointmentsList>
        {filteredAppointments.length === 0 ? (
          <div style={{ color: '#94a3b8', padding: '2rem 0' }}>No {activeTab} appointments found.</div>
        ) : (
          filteredAppointments.map(app => {
            const appDate = parseBackendDate(app.appointmentDateTime);
            return (
              <AppointmentCard key={app.id}>
                <InfoSection>
                  <Avatar><UserIcon size={24} /></Avatar>
                  <Details>
                    <span className="name">{app.client?.firstName} {app.client?.lastName}</span>
                    <span className="time">
                      <CalendarIcon size={14} /> 
                      {appDate ? format(appDate, 'MMM d, yyyy') : 'No Date'}
                      <Clock size={14} style={{ marginLeft: '0.5rem' }} />
                      {appDate ? format(appDate, 'hh:mm a') : 'No Time'}
                    </span>
                  </Details>
                </InfoSection>
                
                <ActionsSection>
                  {activeTab === 'upcoming' ? (
                    <>
                      <Button variant="outline" onClick={() => openRescheduleModal(app)}>
                        <CalendarClock size={16} style={{ marginRight: '0.5rem' }} /> Reschedule
                      </Button>
                      <Button variant="primary" style={{ background: '#22c55e', borderColor: '#22c55e' }} onClick={() => handleComplete(app.id)}>
                        <CheckCircle size={16} style={{ marginRight: '0.5rem' }} /> Complete
                      </Button>
                      <Button variant="danger" onClick={() => handleCancel(app.id)}>
                        <X size={16} style={{ marginRight: '0.5rem' }} /> Cancel
                      </Button>
                    </>
                  ) : (
                    <StatusBadge status={app.appointmentStatus}>{app.appointmentStatus}</StatusBadge>
                  )}
                </ActionsSection>
              </AppointmentCard>
            );
          })
        )}
      </AppointmentsList>

      {isModalOpen && (
        <ModalOverlay onClick={() => setIsModalOpen(false)}>
          <ModalContent onClick={e => e.stopPropagation()}>
            <h3>Reschedule Appointment</h3>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <div>
                <label style={{ fontSize: '0.9rem', color: '#64748b', display: 'block', marginBottom: '0.5rem' }}>Select Date</label>
                <DateSelector>
                  {upcomingDates.map((date, idx) => (
                    <DateCard 
                      key={idx} 
                      selected={selectedRescheduleDate.toDateString() === date.toDateString()}
                      onClick={() => { setSelectedRescheduleDate(date); setSelectedRescheduleTime(null); }}
                    >
                      <div className="day">{format(date, 'EEE')}</div>
                      <div className="date">{format(date, 'dd')}</div>
                    </DateCard>
                  ))}
                </DateSelector>
              </div>

              <div>
                <label style={{ fontSize: '0.9rem', color: '#64748b', display: 'block', marginBottom: '0.5rem' }}>Select Time</label>
                <TimeGrid>
                  {timeSlots.map((slot, idx) => (
                    <TimeSlot 
                      key={idx}
                      selected={selectedRescheduleTime?.time === slot.time}
                      onClick={() => slot.available && setSelectedRescheduleTime(slot)}
                      disabled={!slot.available}
                      style={{ 
                        opacity: slot.available ? 1 : 0.5, 
                        cursor: slot.available ? 'pointer' : 'not-allowed',
                        borderColor: !slot.available ? '#fee2e2' : undefined
                      }}
                    >
                      {slot.label}
                    </TimeSlot>
                  ))}
                </TimeGrid>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end', marginTop: '0.5rem' }}>
              <Button variant="secondary" onClick={() => setIsModalOpen(false)}>Close</Button>
              <Button disabled={!selectedRescheduleTime} onClick={submitReschedule}>Confirm Reschedule</Button>
            </div>
          </ModalContent>
        </ModalOverlay>
      )}
    </PageContainer>
  );
};

export default DoctorSchedule;
