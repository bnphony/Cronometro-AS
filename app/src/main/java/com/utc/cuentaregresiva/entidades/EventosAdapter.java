package com.utc.cuentaregresiva.entidades;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.utc.cuentaregresiva.R;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {
    private List<Evento> mData;
    private LayoutInflater mInflater;
    private Context context;

    private BaseDatos bdd;

    final EventosAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onIntemClick(Evento item);
    }

    public EventosAdapter(List<Evento> itemList, Context context, EventosAdapter.OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public EventosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        bdd = new BaseDatos(this.context);
        return new EventosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventosAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));

    }

    public void setItems(List<Evento> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cv;
        TextView txt_titulo, txt_fecha, txt_estado, txt_tiempo;
        CountDownTimer countDownTimer;
        int colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary);

        ViewHolder(View itemView) {
            super(itemView);
            img_cv = itemView.findViewById(R.id.img_cv);
            txt_titulo = itemView.findViewById(R.id.txt_titulo);
            txt_fecha = itemView.findViewById(R.id.txt_fecha);
            txt_estado = itemView.findViewById(R.id.txt_estado);
            txt_tiempo = itemView.findViewById(R.id.txt_tiempo);

        }

        void bindData(final Evento item) {
//            img_cv.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            txt_titulo.setText(item.getTitulo());
            txt_fecha.setText(item.getFecha() + " " + item.getHora());
            txt_tiempo.setText(calcularTiempoRestante(item.getFecha(), item.getHora()));

            if (item.getImagen() != null) {
                img_cv.setImageBitmap(item.getImagen());
            } else {
                img_cv.setImageResource(R.drawable.reloj);
            }

            final long totalMilis = parseDuration(txt_tiempo.getText().toString());
//            Log.d("YourTag", "Estado: " + item.getEstado() + ", TotalMillis: " + totalMilis);
            if ("Activo".equals(item.getEstado()) && totalMilis > 0) {

                this.txt_tiempo.setTextColor(colorPrimary);
                this.txt_estado.setText("Activo");

                countDownTimer = new CountDownTimer(totalMilis, 1000) {
                    @Override
                    public void onTick(long milisHastaFinalizar) {
                        String tiempo = millisToString(milisHastaFinalizar);
                        txt_tiempo.setText(tiempo);
                    }

                    @Override
                    public void onFinish() {
                        // Mensaje cuando se acaba
                        txt_tiempo.setText(String.format("%d Dias, %02d : %02d : %02d", 0, 0, 0, 0));
                        txt_tiempo.setTextColor(Color.parseColor("#3498db"));
                        txt_estado.setText("Finalizado");
                        bdd.actualizarEstadoEvento(item.getIdEvento(), "Finalizado");
                        countDownTimer.cancel();
                    }
                };
                countDownTimer.start();
            } else {

                this.txt_tiempo.setText(String.format("%d Dias, %02d : %02d : %02d", 0, 0, 0, 0));
                this.txt_tiempo.setTextColor(Color.parseColor("#3498db"));
                this.txt_estado.setText("Finalizado");
                if (item.getEstado() == "Activo") {
                    bdd.actualizarEstadoEvento(item.getIdEvento(), "Finalizado");
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onIntemClick(item);
                }
            });
        }
    }


    public static String calcularTiempoRestante(String fechaFinal, String horaFinal) {
        String formatoTiempoFinal = fechaFinal + "T" + horaFinal + ":00";
        LocalDateTime fechaLimite = LocalDateTime.parse(formatoTiempoFinal, DateTimeFormatter.ISO_DATE_TIME);
        // Fecha Actual
        LocalDateTime fechaActual = LocalDateTime.now();
        // Calcular el tiempo restante
        Duration duration = Duration.between(fechaActual, fechaLimite);
        // Extraer las partes de la duracion restante
        long dias = duration.toDays();
        long horas = duration.toHours() % 24;
        long minutos = duration.toMinutes() % 60;
        long segundos = (duration.toMillis() / 1000) % 60;
        // Dar el formato especifico
        String tiempoRestante = String.format("%d Dias, %02d : %02d : %02d", dias, horas, minutos, segundos);

        if (duration.isNegative()) {
            tiempoRestante = String.format("%d Dias, %02d : %02d : %02d", 0, 0, 0, 0);
        }
        return tiempoRestante;
    }

    private String millisToString(long millis) {
        long segundos = millis / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        long dias = horas / 24;

        return String.format("%d Dias, %02d : %02d : %02d", dias, horas % 24, minutos % 60, segundos % 60);
    }

    private long parseDuration(String tiempoRestante) {
        String [] partes = tiempoRestante.split("[\\s,:\\s]");

        long dias = Long.parseLong(partes[0]);
        long horas = Long.parseLong(partes[3]);
        long minutos = Long.parseLong(partes[6]);
        long segundos = Long.parseLong(partes[9]);
        long totalRestante = (((dias * 24 + horas) * 60 + minutos) * 60 + segundos) * 1000;
        System.out.println(totalRestante);
        return totalRestante;

    }

    /* Nuevo Codigo */
    public void updateDataList(List<Evento> eventos) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DataDiffCallback(mData, eventos));
        mData.clear();
        mData.addAll(eventos);
        diffResult.dispatchUpdatesTo(this);
    }

    private static class DataDiffCallback extends DiffUtil.Callback {
        private final List<Evento> oldList;
        private final List<Evento> newList;

        DataDiffCallback(List<Evento> oldList, List<Evento> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getIdEvento() == newList.get(newItemPosition).getIdEvento();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
