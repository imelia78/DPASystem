import { useState, useEffect, useMemo } from 'react';
import styled from 'styled-components';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { appointmentService } from '../services/api';
import { Button } from '../components/ui/Button';
import { format, addDays } from 'date-fns';
import { ChevronLeft, Calendar as CalendarIcon, Clock, MapPin } from 'lucide-react';
import { useTranslation } from 'react-i18next';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-width: 800px;
  margin: 0 auto;
`;

const BackButton = styled.button`
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: ${({ theme }) => theme.colors.surface};
  color: ${({ theme }) => theme.colors.textMuted};
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: ${({ theme }) => theme.radii.full};
  border: 1px solid ${({ theme }) => theme.colors.border};
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};
  align-self: flex-start;
  
  &:hover {
    color: ${({ theme }) => theme.colors.primary};
    border-color: ${({ theme }) => theme.colors.primary};
    background: ${({ theme }) => theme.colors.surfaceHover};
    transform: translateY(-1px);
  }
`;

const InfoCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const DocHeader = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
  
  h2 {
    font-size: 1.5rem;
    color: ${({ theme }) => theme.colors.text};
  }
`;

const InfoRow = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: 1rem;
`;

const SectionTitle = styled.h3`
  font-size: 1.25rem;
  color: ${({ theme }) => theme.colors.text};
  margin-bottom: 1rem;
  margin-top: 1.5rem;
`;

const DateSelector = styled.div`
  display: flex;
  gap: 1rem;
  overflow-x: auto;
  padding-bottom: 1rem;
  
  &::-webkit-scrollbar {
    height: 6px;
  }
`;

const DateCard = styled.div`
  min-width: 100px;
  background: ${({ selected, theme }) => selected ? theme.colors.primary : theme.colors.surface};
  border: 1px solid ${({ selected, theme }) => selected ? theme.colors.primary : theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.md};
  padding: 1rem;
  text-align: center;
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};

  color: ${({ selected, theme }) => selected ? '#fff' : theme.colors.text};

  &:hover {
    border-color: ${({ theme }) => theme.colors.primary};
  }

  .day { font-size: 0.9rem; margin-bottom: 0.25rem; opacity: 0.9; }
  .date { font-size: 1.25rem; font-weight: 700; }
`;

const TimeGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 1rem;
`;

const TimeSlot = styled.button`
  padding: 1rem;
  border-radius: ${({ theme }) => theme.radii.md};
  font-weight: 600;
  font-size: 1rem;
  transition: ${({ theme }) => theme.transitions.default};
  
  background: ${({ selected, available, theme }) => 
    selected ? theme.colors.primary : 
    !available ? 'rgba(239, 68, 68, 0.1)' : 
    theme.colors.surface
  };
  
  color: ${({ selected, available, theme }) => 
    selected ? '#fff' : 
    !available ? theme.colors.danger : 
    theme.colors.text
  };
  
  border: 1px solid ${({ selected, available, theme }) => 
    selected ? theme.colors.primary : 
    !available ? 'rgba(239, 68, 68, 0.3)' : 
    theme.colors.border
  };
  
  cursor: ${({ available }) => available ? 'pointer' : 'not-allowed'};
  
  &:hover {
    border-color: ${({ available, theme }) => available ? theme.colors.primary : 'rgba(239, 68, 68, 0.3)'};
  }
`;

const upcomingDates = Array.from({ length: 7 }, (_, i) => addDays(new Date(), i + 1));

// Helper to safely parse Spring Boot's LocalDateTime serialization (Array or ISO string)
const parseBackendDate = (dt) => {
  if (!dt) return null;
  if (Array.isArray(dt)) {
    // [year, month, day, hour, minute]
    const [y, m, d, h, min] = dt;
    return new Date(y, m - 1, d, h, min);
  }
  return new Date(dt);
};

const TimeSelection = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();
  const doctor = location.state?.doctor;
  
  const [selectedDate, setSelectedDate] = useState(upcomingDates[0]);
  const [selectedTime, setSelectedTime] = useState(null);
  const [loading, setLoading] = useState(false);

  // Dynamically generate slots and block out existing appointments
  const availableSlots = useMemo(() => {
    if (!doctor) return [];
    
    const slots = [];
    const selectedDateStr = format(selectedDate, 'yyyy-MM-dd');
    
    // Create a Set of "HH:mm" strings representing booked times for this specific date
    const bookedTimes = new Set();
    
    if (doctor.appointments && Array.isArray(doctor.appointments)) {
      doctor.appointments.forEach(app => {
        // Skip cancelled/rejected appointments if needed, but for now block all
        if (app.appointmentStatus === 'REJECTED') return;
        
        const appDate = parseBackendDate(app.appointmentDateTime);
        if (appDate && format(appDate, 'yyyy-MM-dd') === selectedDateStr) {
          bookedTimes.add(format(appDate, 'HH:mm'));
        }
      });
    }

    // Generate standard 30-min slots from 09:00 to 16:30
    for (let hour = 9; hour < 17; hour++) {
      for (let min of [0, 30]) {
        const timeStr = `${hour.toString().padStart(2, '0')}:${min.toString().padStart(2, '0')}`;
        const d = new Date();
        d.setHours(hour, min, 0, 0);
        
        slots.push({
          time: timeStr,
          label: format(d, 'hh:mm a'),
          available: !bookedTimes.has(timeStr)
        });
      }
    }
    return slots;
  }, [selectedDate, doctor]);

  if (!doctor) {
    return <PageContainer>{t('timeSelection.doctorMissing')}</PageContainer>;
  }

  const handleConfirm = async () => {
    if (!selectedTime) return;
    
    setLoading(true);
    try {
      const userStr = localStorage.getItem('user');
      if (!userStr) {
        alert(t('timeSelection.mustBeLoggedIn'));
        return;
      }
      const client = JSON.parse(userStr);

      // Create proper ISO datetime string
      const dateString = format(selectedDate, 'yyyy-MM-dd');
      const dateTimeIso = `${dateString}T${selectedTime.time}:00`;

      // Structure AppointmentRequestDto
      const requestDto = {
        dateTime: dateTimeIso,
        doctorId: doctor.id,
        clientId: client.id,
        address: { city: "New York", district: "Downtown", street: "123 Medical Way" }, // Updated to match Swagger AddressDto
        duration: 30 // standard duration for now
      };

      await appointmentService.create(requestDto);

      navigate('/patient/success', { 
        state: { 
          doctorName: `Dr. ${doctor.lastName}`,
          clinicName: doctor.clinicName || 'Clinic',
          date: format(selectedDate, 'MMM dd, yyyy'),
          time: selectedTime.label,
          address: '123 Medical Way, NY'
        } 
      });
    } catch (err) {
      console.error("Failed to book appointment", err);
      alert(t('timeSelection.bookingFailed'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageContainer>
      <BackButton onClick={() => navigate(-1)}>
        <ChevronLeft size={20} /> {t('timeSelection.back')}
      </BackButton>

      <InfoCard>
        <DocHeader>
          <h2>{t('timeSelection.bookAppointment')} {doctor.lastName}</h2>
        </DocHeader>
        <InfoRow><MapPin size={18} /> {doctor.clinicName || 'HealthBridge Center'} — 123 Medical Way, NY</InfoRow>
        <InfoRow><CalendarIcon size={18} /> {t('timeSelection.selectDateTime')}</InfoRow>
      </InfoCard>

      <div>
        <SectionTitle>{t('timeSelection.selectDate')}</SectionTitle>
        <DateSelector>
          {upcomingDates.map((date, idx) => (
            <DateCard 
              key={idx} 
              selected={selectedDate.toDateString() === date.toDateString()}
              onClick={() => { setSelectedDate(date); setSelectedTime(null); }}
            >
              <div className="day">{format(date, 'EEE')}</div>
              <div className="date">{format(date, 'dd')}</div>
            </DateCard>
          ))}
        </DateSelector>
      </div>

      <div>
        <SectionTitle>{t('timeSelection.selectTime')}</SectionTitle>
        <TimeGrid>
          {availableSlots.map((slot, idx) => (
            <TimeSlot 
              key={idx}
              available={slot.available}
              selected={selectedTime?.time === slot.time}
              onClick={() => slot.available && setSelectedTime(slot)}
              disabled={!slot.available}
            >
              {slot.label}
            </TimeSlot>
          ))}
        </TimeGrid>
      </div>

      <Button 
        fullWidth 
        style={{ padding: '1.25rem', marginTop: '1rem', fontSize: '1.1rem' }}
        disabled={!selectedTime || loading}
        onClick={handleConfirm}
      >
        {loading ? t('timeSelection.confirming') : t('timeSelection.confirmBooking')}
      </Button>
    </PageContainer>
  );
};

export default TimeSelection;
